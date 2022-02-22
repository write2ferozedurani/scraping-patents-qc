package com.scraping.patents.service;

import com.scraping.patents.model.Patent;

import java.util.List;

public interface PatentService {
    String processPatents(String patentList);
    List<Patent> getPatents();
}
