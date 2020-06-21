package com.kpi.diplom.riskassessment.repository;

import com.kpi.diplom.riskassessment.model.ExposureFactor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExposureFactorRepository extends CrudRepository<ExposureFactor, Integer> {
}
