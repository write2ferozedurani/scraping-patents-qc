package com.scraping.patents.service.impl;

import com.scraping.patents.service.OCRDocumentService;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
@Slf4j
public class OCRDocumentServiceImpl implements OCRDocumentService {

    /**
     * This method is used to perform OCR on a given Patent Document
     * @param file
     * @param appNo
     * @return OCR File
     */
    @Override
    public File convertPdfToText(File file, String appNo) {
        try {
            File tempOutputFile = File.createTempFile("TempFile" + appNo, ".txt");
            FileWriter fileWriter = new FileWriter(tempOutputFile);

            PDDocument document = PDDocument.load(file);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int index = 0; index < document.getNumberOfPages(); index++) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(index, 300, ImageType.RGB);
                File tempImageFile = File.createTempFile("TempFile", ".png");
                ImageIO.write(bufferedImage, "png", tempImageFile);
                fileWriter.write(getDataFromImage(tempImageFile));
                tempImageFile.delete();
            }
            document.close();
            fileWriter.close();
            return tempOutputFile;
        } catch (IOException e) {
            log.error("Error while processing pdf to image", e);
        }
        return null;
    }

    /**
     * This method used to perform OCR process on a snapshot image of PDF Content
     * It uses Tesseract to perform OCR
     * @param file
     * @return String
     */
    private String getDataFromImage(File file) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(System.getenv("TESSDATA_PREFIX"));
        tesseract.setLanguage("eng");
        try {
            return new StringBuilder().append(tesseract.doOCR(file)).toString();
        } catch (TesseractException e) {
            log.error("Error while processing image to text", e);
        }
        return null;
    }
}
