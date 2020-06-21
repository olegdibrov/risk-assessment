package com.kpi.diplom.riskassessment.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Emission {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String region;

    private String lat;

    private String lng;

    @OneToMany(mappedBy="emission")
    private List<EmissionInfo> emissionInfos;

}
