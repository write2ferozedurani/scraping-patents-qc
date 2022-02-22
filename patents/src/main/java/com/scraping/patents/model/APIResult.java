package com.scraping.patents.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class APIResult implements Serializable {
    private List<PatentDetail> results;
    private int recordTotalQuantity;
}
