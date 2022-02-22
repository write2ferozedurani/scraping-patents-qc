package com.scraping.patents;

import com.scraping.patents.dao.PatentDao;
import com.scraping.patents.model.Patent;
import com.scraping.patents.service.PatentService;
import com.scraping.patents.service.S3StorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
class PatentsApplicationTests {

    @MockBean
    S3StorageService s3StorageService;

    @MockBean
    PatentDao patentDao;

    @Autowired
    PatentService patentService;

    // juni 4(junit.) , junit5( jupitar.)
    @Test
    void contextLoads() {
        // mockito jar: to mock any third party calls.

        when(patentDao.getPatents()).thenReturn(null);
        List<Patent> patents = patentService.getPatents();
        Assertions.assertNull(patents);

        List<Patent> list = new ArrayList<>();
        Patent p = new Patent();
        list.add(p);
        when(patentDao.getPatents()).thenReturn(list);
        patents = patentService.getPatents();
        Assertions.assertNotNull(patents);

    }

}
