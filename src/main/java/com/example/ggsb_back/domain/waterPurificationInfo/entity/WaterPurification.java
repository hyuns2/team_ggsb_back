package com.example.ggsb_back.domain.waterPurificationInfo.entity;


import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "WATER_PURIFICATION")
@NoArgsConstructor
public class WaterPurification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WP_ID")
    private Long WPID;

    @Column
    private Integer TYPE;

    @Column(name = "WP_NAME")
    private String WPNAME;

    public Long getWPID() {
        return WPID;
    }

    public Integer getTYPE() {
        return TYPE;
    }

    public String getWPNAME() {
        return WPNAME;
    }
}
