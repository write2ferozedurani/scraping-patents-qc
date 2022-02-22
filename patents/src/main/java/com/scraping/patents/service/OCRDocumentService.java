package com.scraping.patents.service;

import java.io.File;

public interface OCRDocumentService {
    File convertPdfToText(File file, String appNo);
}
