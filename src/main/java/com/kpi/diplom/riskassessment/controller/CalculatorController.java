package com.kpi.diplom.riskassessment.controller;

import com.kpi.diplom.riskassessment.model.Substance;
import com.kpi.diplom.riskassessment.repository.CancerogenSubstanceRepository;
import com.kpi.diplom.riskassessment.repository.SubstanceRepository;
import com.kpi.diplom.riskassessment.service.SubstanceService;
import com.kpi.diplom.riskassessment.service.calculator.risk.AverageDailyDoseCalculator;
import com.kpi.diplom.riskassessment.service.calculator.risk.NonCarcinogenicCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CalculatorController {

    private final SubstanceRepository substanceRepository;

    private final CancerogenSubstanceRepository cancerogenSubstanceRepository;

    private final SubstanceService substanceService;

    private final NonCarcinogenicCalculator nonCarcinogenicCalculator;

    private final AverageDailyDoseCalculator averageDailyDoseCalculator;

    @GetMapping(value = "/calculator")
    public String fillCalculatorInfo(Model model) {
        model.addAttribute("substances", substanceRepository.findAll());
        model.addAttribute("cancerogenSubstances", cancerogenSubstanceRepository.findAll());
        model.addAttribute("chosenSubstances", new ArrayList<Substance>());
        model.addAttribute("btnCalculate", false);
        return "calculator";

    }

    @PostMapping(value = "/calculate-non-conc-assessment")
    @ResponseBody
    public String calculateNonConcAssessment(@RequestParam(value = "substance", required = false) String substance,
                                             @RequestParam(value = "concentration", required = false) String concentration) {
        return nonCarcinogenicCalculator.calculateRisk(substance, concentration);
    }

    @PostMapping(value = "/calculate-amount-of-non-conc-assessment")
    @ResponseBody
    public String calculateAmountOfNonCancAssesment(@RequestParam(value = "id", required = false) List<String> substances,
                                                    @RequestParam(value = "dose", required = false) List<String> doses) {
        return nonCarcinogenicCalculator.calculateSumRisk(substances, doses);
    }

    @PostMapping(value = "/getChosenSubstancesInfo")
    public String fillChosenSubstancesInfo(Model model,
                                           @RequestParam(value = "chosen", required = false) List<String> substances) {

        model.addAttribute("substances", substanceRepository.findAll());
        model.addAttribute("chosenSubstances", substanceService.getChosenSubstancesByName(substances));
        model.addAttribute("cancerogenSubstances", cancerogenSubstanceRepository.findAll());
        model.addAttribute("btnCalculate", true);
        return "calculator";
    }

    @PostMapping(value = "/average-daily-dose")
    @ResponseBody
    public String calculateAverageDailyDose(@RequestParam(value = "substance", required = false) String substance,
                                            @RequestParam(value = "concentration1", required = false) String concentration,
                                            @RequestParam(value = "numOfPeoples", required = false) String numOfPeoples) {
        return averageDailyDoseCalculator.calcAverageDailyDose(substance, concentration, numOfPeoples);
    }
}
