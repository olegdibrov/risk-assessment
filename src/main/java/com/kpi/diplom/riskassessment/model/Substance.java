package com.kpi.diplom.riskassessment.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
public class Substance {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "rfc")
    private BigDecimal rfc;

    @Column(name = "rfd")
    private BigDecimal rfd;

    @OneToMany(mappedBy = "substance")
    private List<SubstanceCriticalOrgans> criticalOrgans;

    public String toStringCriticalOrgans() {
        return criticalOrgans.stream().map(x -> x.getCriticalOrgan().getName()).collect(Collectors.joining(", "));
    }

}
