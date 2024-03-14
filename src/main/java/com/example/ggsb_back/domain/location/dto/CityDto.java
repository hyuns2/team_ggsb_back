package com.example.ggsb_back.domain.location.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CityDto {
    private String state;
    private List<String> city;
}
