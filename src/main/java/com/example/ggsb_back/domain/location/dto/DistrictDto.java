package com.example.ggsb_back.domain.location.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DistrictDto {
    private String city;
    private List<String> district;
}
