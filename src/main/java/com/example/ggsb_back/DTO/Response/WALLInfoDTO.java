package com.example.ggsb_back.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class WALLInfoDTO {
    private String city;
    private String district;
    private Double pHVal;
    private Double tbVal;
    private Double clVal;
}
