package com.kpi.diplom.riskassessment.service;

import com.kpi.diplom.riskassessment.model.Substance;
import com.kpi.diplom.riskassessment.repository.SubstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubstanceService {

    @Autowired
    private SubstanceRepository substanceRepository;

    public List<Substance> getChosenSubstancesByName(List<String> substances) {
        return substances.stream()
                .map(substance -> substanceRepository.findByName(substance))
                .collect(Collectors.toList());
    }

    public List<Substance> getChosenSubstancesById(List<String> substancesId) {
        List<Integer> ids =  substancesId.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return (List<Substance>) substanceRepository.findAllById(ids);
    }
}
