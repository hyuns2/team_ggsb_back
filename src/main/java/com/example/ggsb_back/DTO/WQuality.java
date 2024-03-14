package com.example.ggsb_back.DTO;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;


@Data
public class WQuality {

    private String fcltyMngNm;

    private String phVal;

    private String tbVal;

    private String clVal;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date datetime;

    private String occrrncDt;

    private String dt;
}
