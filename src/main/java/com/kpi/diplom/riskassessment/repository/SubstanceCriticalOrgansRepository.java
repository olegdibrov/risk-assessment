package com.kpi.diplom.riskassessment.repository;

import com.kpi.diplom.riskassessment.model.SubstanceCriticalOrgans;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubstanceCriticalOrgansRepository extends CrudRepository<SubstanceCriticalOrgans, Integer> {
}
