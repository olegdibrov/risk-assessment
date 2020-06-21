package com.kpi.diplom.riskassessment.model;

import javax.persistence.*;

@Entity
public class EmissionInfo {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private Integer year;

    private String htmlContent;

    @ManyToOne
    @JoinColumn(name="emission_id", nullable=false)
    private Emission emission;
}
