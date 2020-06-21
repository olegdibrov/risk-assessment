package com.kpi.diplom.riskassessment.repository;

import com.kpi.diplom.riskassessment.model.CancerogenSubstance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancerogenSubstanceRepository extends CrudRepository<CancerogenSubstance, Integer> {

    CancerogenSubstance findByName(String name);
}
