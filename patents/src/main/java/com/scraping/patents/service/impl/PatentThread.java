package com.scraping.patents.service.impl;

import com.scraping.patents.dao.PatentDao;
import com.scraping.patents.model.Patent;
import com.scraping.patents.model.PatentDetail;
import com.scraping.patents.service.OCRDocumentService;
import com.scraping.patents.service.S3StorageService;
import com.scraping.patents.util.PatentConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("prototype")
@Slf4j
public class PatentThread implements  Runnable {

    @Autowired
    private S3StorageService s3StorageService;

    @Autowired
    private OCRDocumentService ocrDocumentService;

    @Autowired
    private PatentDao patentDao;

    @Value("${application.bucket.url}")
    private String s3BucketUrl;

    private Map<String, String> patentStatusMap = new ConcurrentHashMap<>();
    private PatentDetail patentDetail;

    // Constructor with two parameters
    public PatentThread(Map<String, String> patentStatusMap, PatentDetail patentDetail) {
        this.patentStatusMap = patentStatusMap;
        this.patentDetail = patentDetail;
    }

    @Override
    public void run() {

        if (patentStatusMap.get(patentDetail.getPatentApplicationNumber())
                .equals(PatentConstants.PATENT_PROCESS_STATUS_NOT_STARTED)) {

            String threadName = Thread.currentThread().getName();
            log.info("Picked up by "+ threadName + " app no. is " +patentDetail.getPatentApplicationNumber());
            log.info(threadName
                    + " started processing at: "
                    + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())
                    + " for " +patentDetail.getPatentApplicationNumber());

            File patentDocument = getFileFromURL(patentDetail.getFilelocationURI(), patentDetail.getPatentApplicationNumber());

            log.info("Storing OCR document to S3...");
            // Saving OCR document to S3
            s3StorageService.uploadFileToS3(
                    ocrDocumentService.convertPdfToText(patentDocument, patentDetail.getPatentApplicationNumber()),
                    createFileName(patentDetail, PatentConstants.OCR_TEXT));

            log.info("Storing original patent document to S3...");
            // Saving Original Patent document to S3
            s3StorageService.uploadFileToS3(
                    patentDocument,
                    createFileName(patentDetail, PatentConstants.BLANK_SPACE));

            patentDocument.delete();

            log.info("Saving patent metadata details to Dynamo DB...");
            // Saving Patent Metadata details to Dynamo DB
            patentDao.addPatent(getPatentObjectToSave(patentDetail));

            patentStatusMap.replace(patentDetail.getPatentApplicationNumber(), PatentConstants.PATENT_PROCESS_STATUS_COMPLETED);
            log.info(threadName
                    + " completed processing at: "
                    + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date())
                    + " for " +patentDetail.getPatentApplicationNumber());
        }
    }

    /**
     * Sub Method to retrieve file object from a given S3 url
     * @param fileUrl
     * @param appNo
     * @return File Object
     */
    private synchronized File getFileFromURL(String fileUrl, String appNo) {
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
     * @param patentDetail
     * @param constant
     * @return filename
     */
    private String createFileName(PatentDetail patentDetail, String constant) {
        return new StringBuffer()
                .append(constant)
                .append(patentDetail.getPatentApplicationNumber())
                .append(PatentConstants.UNDER_SCORE)
                .append(patentDetail.getPatentNumber())
                .toString();
    }

    /**
     * Sub Method to set Patent Metadata that is to be stored to DynamoDB
     * @param patentDetail
     * @return Patent Meta Data Object
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
