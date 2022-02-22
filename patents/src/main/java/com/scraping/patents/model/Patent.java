package com.scraping.patents.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Patents")
@DynamoDBDocument
public class Patent implements Serializable {

    @DynamoDBHashKey (attributeName = "patentApplicationNumber")
    private String patentApplicationNumber;

    @DynamoDBAttribute
    private String inventionSubjectMatterCategory;

    @DynamoDBAttribute
    private String filingDate;

    @DynamoDBAttribute
    private String assigneeEntityName;

    @DynamoDBAttribute
    private String assigneePostalAddressText;

    @DynamoDBAttribute
    private String inventionTitle;

    @DynamoDBAttribute
    private String filelocationURI;

    @DynamoDBAttribute
    private String s3FilelocationURI;

    @DynamoDBAttribute
    private String grantDocumentIdentifier;

    @DynamoDBAttribute
    private String grantDate;

    @DynamoDBAttribute
    private String patentNumber;
}
