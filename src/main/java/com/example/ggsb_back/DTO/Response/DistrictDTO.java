package com.example.ggsb_back.DTO.Response;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DistrictDTO {
    private String city;
    private List<String> district;
}
