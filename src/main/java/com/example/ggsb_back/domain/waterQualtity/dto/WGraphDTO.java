package com.example.ggsb_back.domain.waterQualtity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WGraphDTO {
    private String city;
    private String district;
    private WPurificationDTO waterPurification;
    private List<String> dates;
    private List<Double> pHVals;
    private List<Double> tbVals;
    private List<Double> clVals;
}
