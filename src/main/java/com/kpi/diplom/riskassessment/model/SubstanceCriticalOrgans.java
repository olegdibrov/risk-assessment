package com.kpi.diplom.riskassessment.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class SubstanceCriticalOrgans {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "substance_id")
    private Substance substance;

    @ManyToOne
    @JoinColumn(name = "criticalOrgan_id")
    private CriticalOrgan criticalOrgan;

}
