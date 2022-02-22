package com.scraping.patents.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.scraping.patents.service.S3StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class S3StorageServiceImpl implements S3StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Value("${application.bucket.url}")
    private String s3BucketUrl;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private TransferManager transferManager;

    @Override
    public String uploadFileToS3 (File file, String filename) {
        s3Client.putObject(bucketName, filename, file);
        file.delete();
        return "File uploaded..";
    }
}
