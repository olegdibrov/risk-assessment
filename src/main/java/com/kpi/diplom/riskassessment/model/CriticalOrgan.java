package com.kpi.diplom.riskassessment.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class CriticalOrgan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "criticalOrgan")
    private List<SubstanceCriticalOrgans> substances;

}
