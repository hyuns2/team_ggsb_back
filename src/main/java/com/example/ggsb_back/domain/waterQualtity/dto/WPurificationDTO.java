package com.example.ggsb_back.domain.waterQualtity.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class WPurificationDTO {
    private String wName;
    private Integer type;
}
