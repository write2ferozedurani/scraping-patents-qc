package com.scraping.patents.util;

public class PatentConstants {
    public static final int CONNECT_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 10000;
    public static final String UNDER_SCORE = "_";
    public static final String BLANK_SPACE = "";
    public static final String OCR_TEXT = "OCR_";
    public static final String PATENT_PROCESS_STATUS_NOT_STARTED="NotStarted";
    public static final String PATENT_PROCESS_STATUS_IN_PROGRESS ="InProgress";
    public static final String PATENT_PROCESS_STATUS_COMPLETED ="Completed";
    public static final String PATENT_PROCESS_IN_PROGRESS_USER_MESSAGE="Patent(s) Processing is in progress and User will be updated when process gets completed.";
    public static final String PATENT_PROCESS_INVALID_PATENTS_USER_MESSAGE="No valid patents to process";
    public static final String BASE_API_PUBS_URL = "https://developer.uspto.gov/ibd-api/v1/application/publications?patentApplicationNumber=";
    public static final String BASE_API_GRANTS_URL = "https://developer.uspto.gov/ibd-api/v1/application/grants?patentApplicationNumber=";

}
