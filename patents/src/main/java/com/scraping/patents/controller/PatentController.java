package com.scraping.patents.controller;

import com.scraping.patents.model.Patent;
import com.scraping.patents.service.PatentService;
import com.scraping.patents.service.S3StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patents")
public class PatentController {

    @Autowired
    private S3StorageService s3StorageService;
    @Autowired
    private PatentService patentService;

    /**
     * Rest Method to return list of patents
     * @return Patent List
     */
    @GetMapping("/list")
    public List<Patent> getPatents(){
        return patentService.getPatents();
    }

    /**
     * Rest Method to process Scraping of Patents based on
     * comma separated patent(s) param
     * @param patentList
     * @return String response message to the user
     */
    @PostMapping("/process")
    public String processPatentData(@RequestBody(required = false) String patentList) {
        return patentService.processPatents(patentList);
    }
}
