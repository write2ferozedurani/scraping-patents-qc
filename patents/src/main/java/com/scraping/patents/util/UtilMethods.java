package com.scraping.patents.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
public class UtilMethods {

    // Method to Multipart File to Regular File
    public static File convertMultipartFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(convertedFile);
            fileOutputStream.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error Converting Multipart File", e);
        }
        return convertedFile;
    }
}
