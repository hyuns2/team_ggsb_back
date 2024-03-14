package com.example.ggsb_back.Entity;


import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "WATER_LOCATION")
@NoArgsConstructor
public class WaterLocation {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOC_ID")
    private Long LOCID;

    @Column
    private String STATE;

    @Column
    private String CITY;

    @Column
    private String DISTRICT;

    @Column(name = "WP_ID")
    private Long WPID;

    public Long getLOCID() {
        return LOCID;
    }

    public String getSTATE() {
        return STATE;
    }

    public String getCITY() {
        return CITY;
    }

    public String getDISTRICT() {
        return DISTRICT;
    }

    public Long getWPID() {
        return WPID;
    }
}
