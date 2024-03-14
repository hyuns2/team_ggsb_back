package com.example.ggsb_back.DTO.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CityDTO {
    private String state;
    private List<String> city;
}
