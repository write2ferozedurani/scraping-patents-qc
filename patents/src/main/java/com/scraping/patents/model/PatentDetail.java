package com.scraping.patents.model;

import lombok.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatentDetail implements Serializable {
    private String inventionSubjectMatterCategory;
    private String patentApplicationNumber;
    private String filingDate;
    private String mainCPCSymbolText;
    private String furtherCPCSymbolArrayText;
    private String[] inventorNameArrayText;
    private String[] abstractText;
    private String assigneeEntityName;
    private String assigneePostalAddressText;
    private String inventionTitle;
    private String filelocationURI;
    private String[] claimText;

    // Additional elements for Grant type Patents
    private String[] descriptionText;
    private String grantDocumentIdentifier;
    private String grantDate;
    private String patentNumber;

    // Additional elements for Publication type Patents
    private String publicationDate;
    private String publicationDocumentIdentifier;
}
