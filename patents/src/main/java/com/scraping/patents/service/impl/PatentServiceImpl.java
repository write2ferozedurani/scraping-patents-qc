package com.scraping.patents.service.impl;

import com.scraping.patents.dao.PatentDao;
import com.scraping.patents.model.APIResult;
import com.scraping.patents.model.Patent;
import com.scraping.patents.model.PatentDetail;
import com.scraping.patents.service.OCRDocumentService;
import com.scraping.patents.service.PatentService;
import com.scraping.patents.service.S3StorageService;
import com.scraping.patents.util.PatentConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class PatentServiceImpl implements PatentService{

    @Autowired
    private S3StorageService s3StorageService;

    @Autowired
    private OCRDocumentService ocrDocumentService;

    @Autowired
    private PatentDao patentDao;

    @Value("${application.bucket.url}")
    private String s3BucketUrl;

    @Autowired
    private ApplicationContext applicationContext;

    // This variable is for multi-thread version of OCR Document process
    private volatile Map<String, String> patentProcessStatusMap;

    /**
     * Service Method to get list of Patents that are already being processed
     * @return List of Patents
     */
    @Override
    public List<Patent> getPatents() {
        return patentDao.getPatents();
    }

    /**
     * Service Method to process given Patents based on Application Numbers
     * 1. Retrieve Patent(s) metadata using Developer API (External REST Call) of USPTO
     * 2. Generate Patent Document based on USPTO S3 url link of patent
     * 3. Performs OCR Process on given patent document and save it to Amazon S3
     *      - Utilized Thread Pool concept to improvise performance and best CPU utilization
     * 4. Performs saving of original Patent Document to Amazon S3
     * 5. Performs saving of patent metadata to DynamoDB
     * @param patentList
     * @return response message to user
     */
    @Override
    public String processPatents(String patentList) {

        // Establish REST Template to retrieve patent metadata, including url to Patent Document
        ResponseEntity<APIResult> patentDetailsResponseEntity
                = getPatentDetails(getExternalRESTApiUrl(patentList));

        if (patentDetailsResponseEntity.getBody().getRecordTotalQuantity() > 0) {
            List<PatentDetail> patentDetailList = patentDetailsResponseEntity.getBody().getResults();

            // Using Thread Pool - Process OCR Document
            ocrProcessMultiThreading(patentDetailList);

            // Usual Process in a Single Thread Environment - Process OCR Document
            //ocrProcessSingleThread(patentDetailList);

            return PatentConstants.PATENT_PROCESS_IN_PROGRESS_USER_MESSAGE;
        } else {
            return PatentConstants.PATENT_PROCESS_INVALID_PATENTS_USER_MESSAGE;
        }
    }

    /**
     * Sub Method to perform OCR process of Patent Documents using ThreadPool
     * Utilized Executor Service of Java to create Thread Pool
     * Just created 2 threads to perform the task, but this can be decided based on underlying hardware
     * @param patentDetailList
     */
    private void ocrProcessMultiThreading(List<PatentDetail> patentDetailList) {
        log.info("Thread Pool Started at: "+ new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));

        patentProcessStatusMap = createPatentStatusMap(patentDetailList);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        patentDetailList.stream().forEach(patentDetail -> {
            PatentThread patentThread = new PatentThread(patentProcessStatusMap, patentDetail);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(patentThread);
            executor.execute(patentThread);
        });
        executor.shutdown();
    }

    /**
     * Sub Method to create list of Patent(s) with initial status of "Not Started"
     * It will be utilized in document process via Multi-Threading
     * @param patentDetailList
     * @return Concurrent HashMap
     */
    private ConcurrentHashMap<String, String> createPatentStatusMap(List<PatentDetail> patentDetailList) {
        Map<String, String> patentProcessStatusMap = new ConcurrentHashMap<String, String>();
        patentDetailList.forEach(patent -> {
            patentProcessStatusMap.put(patent.getPatentApplicationNumber(),
                    PatentConstants.PATENT_PROCESS_STATUS_NOT_STARTED);
        });
        return (ConcurrentHashMap<String, String>) patentProcessStatusMap;
    }

    /**
     * Sub Method to retrieve Patent Metadata for list of given Patent Application Numbers
     * Created Rest Template to invoke external REST call to USPTO website (Single hit to the source)
     * @param externalRESTUrl
     * @return JSON Object of Patents
     */
    private ResponseEntity<APIResult> getPatentDetails(String externalRESTUrl) {
        log.info("Getting patent metadata from external REST url..");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate.getForEntity(externalRESTUrl, APIResult.class);
    }

    /**
     * Sub method to form external REST API URL
     * @param patentList
     * @return
     */
    private String getExternalRESTApiUrl(String patentList) {
        return new StringBuilder()
                .append(PatentConstants.BASE_API_GRANTS_URL)
                .append(patentList).toString();
    }

    /**
     * Sub Method to perform OCR Process on given Patents
     * This is not being utilized and code is commented in the above callable method
     * @param patentDetailList
     */
    private void ocrProcessSingleThread(List<PatentDetail> patentDetailList) {
        patentDetailList.stream().forEach(patentDetail -> {
            File patentDocument = getFileFromURL(patentDetail.getFilelocationURI(), patentDetail.getPatentApplicationNumber());

            System.out.println("Storing OCR document to S3...");
            // Saving OCR document to S3
            s3StorageService.uploadFileToS3(
                    ocrDocumentService.convertPdfToText(patentDocument, patentDetail.getPatentApplicationNumber()),
                    createFileName(patentDetail, PatentConstants.OCR_TEXT));

            System.out.println("Storing document to S3...");
            // Saving Original Patent document to S3
            s3StorageService.uploadFileToS3(
                    patentDocument,
                    createFileName(patentDetail, PatentConstants.BLANK_SPACE));

            patentDocument.delete();

            System.out.println("Before Dynamo DB call...");
            // Saving Patent Metadata details to Dynamo DB
            patentDao.addPatent(getPatentObjectToSave(patentDetail));
        });
    }

    /**
     * Sub Method to retrieve file object from a given S3 url
     * This is not being utilized and as it is for regular processing without threads
     * @param fileUrl
     * @param appNo
     * @return File Object
     */
    private File getFileFromURL(String fileUrl, String appNo) {
        try {
            File file = File.createTempFile("tempPatent" +appNo,".pdf");
            FileUtils.copyURLToFile(
                    new URL(fileUrl),
                    file,
                    PatentConstants.CONNECT_TIMEOUT,
                    PatentConstants.READ_TIMEOUT
            );
            return file;
        } catch (IOException e) {
            log.error("File Download Error Occurred.." + e);
        }
        return null;
    }

    /**
     * Sub Method to form filename that is to be stored to AmazonS3
     * This is not being utilized and as it is for regular processing without threads
     * @param patentDetail
     * @param constant
     * @return
     */
    private String createFileName(PatentDetail patentDetail, String constant) {
        return new StringBuilder()
                .append(constant)
                .append(patentDetail.getPatentApplicationNumber())
                .append(PatentConstants.UNDER_SCORE)
                .append(patentDetail.getPatentNumber())
                .toString();
    }

    /**
     * Sub Method to set Patent Metadata that is to be stored to DynamoDB
     * This is not being utilized and as it is for regular processing without threads
     * @param patentDetail
     * @return
     */
    private Patent getPatentObjectToSave(PatentDetail patentDetail) {
        return new Patent(
                patentDetail.getPatentApplicationNumber(),
                patentDetail.getInventionSubjectMatterCategory(),
                patentDetail.getFilingDate(),
                patentDetail.getAssigneeEntityName(),
                patentDetail.getAssigneePostalAddressText(),
                patentDetail.getInventionTitle(),
                patentDetail.getFilelocationURI(),
                new StringBuilder()
                        .append(s3BucketUrl)
                        .append(createFileName(patentDetail, PatentConstants.BLANK_SPACE)).toString(),
                patentDetail.getGrantDocumentIdentifier(),
                patentDetail.getGrantDate(),
                patentDetail.getPatentNumber()
        );
    }
}
