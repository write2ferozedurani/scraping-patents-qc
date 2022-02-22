package com.scraping.patents.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface S3StorageService {
    String uploadFileToS3 (File file, String filename);
}
