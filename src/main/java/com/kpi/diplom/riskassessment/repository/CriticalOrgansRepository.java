package com.kpi.diplom.riskassessment.repository;

import com.kpi.diplom.riskassessment.model.CriticalOrgan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriticalOrgansRepository extends CrudRepository<CriticalOrgan, Integer> {

    CriticalOrgan findByName(String name);
}
