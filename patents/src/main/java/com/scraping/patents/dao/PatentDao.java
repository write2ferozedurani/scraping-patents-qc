package com.scraping.patents.dao;

import com.scraping.patents.model.Patent;

import java.util.List;

public interface PatentDao {
    // Abstract Dao Methods
    Patent addPatent(Patent patent);
    List<Patent> getPatents();
}
