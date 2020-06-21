package com.kpi.diplom.riskassessment.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class ExposureFactor {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private BigDecimal bodyWeight;

    private BigDecimal inhalationSpeedIndoor;

    private BigDecimal inhalationSpeedOutdoor;

    private BigDecimal timeIndoors;

    private BigDecimal timeOutdoors;

    private BigDecimal frequencyOfExposure;

    private BigDecimal durationOfExposure;

    private BigDecimal exposureAveragingPeriod;


}
