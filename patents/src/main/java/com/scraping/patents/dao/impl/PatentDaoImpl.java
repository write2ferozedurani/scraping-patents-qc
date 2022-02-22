package com.scraping.patents.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.scraping.patents.dao.PatentDao;
import com.scraping.patents.model.Patent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PatentDaoImpl implements PatentDao {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    /**
     * Method saves patent meta data details to DynamoDB
     * @param patent
     * @return
     */
    @Override
    public Patent addPatent(Patent patent) {
        dynamoDBMapper.save(patent);
        return patent;
    }

    /**
     * Method uses DynamoDB Mapper to extract patents from DynamoDB
     * @return List of Patents
     */
    @Override
    public List<Patent> getPatents() {
        return dynamoDBMapper.scan(Patent.class, new DynamoDBScanExpression());
    }
}
