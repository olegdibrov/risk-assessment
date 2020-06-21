package com.kpi.diplom.riskassessment.repository;

import com.kpi.diplom.riskassessment.model.Substance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubstanceRepository extends CrudRepository<Substance, Integer> {

    Substance findByName(String name);
}
