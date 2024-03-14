package com.example.ggsb_back.domain.location.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Table(name = "WATER_LOCATION")
@Entity
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
}
