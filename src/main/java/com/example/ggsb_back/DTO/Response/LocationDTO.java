package com.example.ggsb_back.DTO.Response;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LocationDTO {
    private String state;
    private String city;
    private String district;

}
