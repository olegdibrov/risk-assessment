package com.kpi.diplom.riskassessment.controller;

import com.kpi.diplom.riskassessment.model.CancerogenSubstance;
import com.kpi.diplom.riskassessment.model.Substance;
import com.kpi.diplom.riskassessment.model.SubstanceCriticalOrgans;
import com.kpi.diplom.riskassessment.repository.CancerogenSubstanceRepository;
import com.kpi.diplom.riskassessment.repository.CriticalOrgansRepository;
import com.kpi.diplom.riskassessment.repository.SubstanceCriticalOrgansRepository;
import com.kpi.diplom.riskassessment.repository.SubstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor
public class SubstancesController {

    private final  SubstanceRepository substanceRepository;

    private final CancerogenSubstanceRepository cancerogenSubstanceRepository;

    private final SubstanceCriticalOrgansRepository substanceCriticalOrgansRepository;

    private final CriticalOrgansRepository criticalOrgansRepository;


    @GetMapping(value = "/admin")
    public String fillAdminModel(Model model) {
        model.addAttribute("substances", substanceRepository.findAll());
        model.addAttribute("cancerogenSubstances", cancerogenSubstanceRepository.findAll());
        model.addAttribute("organs", criticalOrgansRepository.findAll());
        model.addAttribute("chosenOrgans", "");
        return "admin";
    }

    @PostMapping(value = "/update-substance-info")
    public String updateSubstanceInfo(@RequestParam(value = "id", required = false) String id,
                                      @RequestParam(value = "rfc", required = false) String rfc,
                                      @RequestParam(value = "rfd", required = false) String rfd,
                                      Model model) {

        Substance substance = substanceRepository.findById(Integer.parseInt(id)).get();
        substance.setRfc(new BigDecimal(rfc));
        substance.setRfd(new BigDecimal(rfd));
        substanceRepository.save(substance);
        model.addAttribute("substances", substanceRepository.findAll());
        model.addAttribute("cancerogenSubstances", cancerogenSubstanceRepository.findAll());
        model.addAttribute("organs", criticalOrgansRepository.findAll());
        model.addAttribute("chosenOrgans", "");
        return "admin";
    }

    @PostMapping(value = "/update-cancerogen-substance-info")
    public String updateCancerogenSubstanceInfo(@RequestParam(value = "id", required = false) String id,
                                                @RequestParam(value = "sf", required = false) String sf,
                                                Model model) {
        CancerogenSubstance substance = cancerogenSubstanceRepository.findById(Integer.parseInt(id)).get();
        substance.setSf(new BigDecimal(sf));
        cancerogenSubstanceRepository.save(substance);
        model.addAttribute("substances", substanceRepository.findAll());
        model.addAttribute("cancerogenSubstances", cancerogenSubstanceRepository.findAll());
        model.addAttribute("organs", criticalOrgansRepository.findAll());
        model.addAttribute("chosenOrgans", "");
        return "admin";
    }

    @PostMapping(value = "/save-cancerogen-substance-info")
    public String saveCancerogenSubstanceInfo(@RequestParam(value = "name") String name,
                                              @RequestParam(value = "sf") String sf,
                                              Model model) {
        CancerogenSubstance cancerogenSubstance = new CancerogenSubstance();
        cancerogenSubstance.setName(name);
        cancerogenSubstance.setSf(new BigDecimal(sf));
        cancerogenSubstanceRepository.save(cancerogenSubstance);
        model.addAttribute("substances", substanceRepository.findAll());
        model.addAttribute("cancerogenSubstances", cancerogenSubstanceRepository.findAll());
        model.addAttribute("organs", criticalOrgansRepository.findAll());
        model.addAttribute("chosenOrgans", "");
        return "admin";
    }

    @PostMapping(value = "/save-substance-info")
    public String saveSubstanceInfo(@RequestParam(value = "name") String name,
                                    @RequestParam(value = "rfc") String rfc,
                                    @RequestParam(value = "rfd") String rfd,
                                    @RequestParam(value = "chosenOrgans", required = false) List<String> chosenOrgans,
                                    Model model) {
        Substance substance = new Substance();
        substance.setName(name);
        substance.setRfc(new BigDecimal(rfc));
        substance.setRfd(new BigDecimal(rfd));
        substance = substanceRepository.save(substance);


        List<SubstanceCriticalOrgans> substanceCriticalOrgans = chosenOrgans.stream().map(organ-> {
            SubstanceCriticalOrgans substanceCriticalOrgans1 = new SubstanceCriticalOrgans();
            substanceCriticalOrgans1.setCriticalOrgan(criticalOrgansRepository.findByName(organ));
            substanceCriticalOrgans1.setSubstance(substanceRepository.findByName(name));
            return substanceCriticalOrgansRepository.save(substanceCriticalOrgans1);
        }).collect(Collectors.toList());
        substance.setCriticalOrgans(substanceCriticalOrgans);
        substanceRepository.save(substance);

        model.addAttribute("substances", substanceRepository.findAll());
        model.addAttribute("cancerogenSubstances", cancerogenSubstanceRepository.findAll());
        model.addAttribute("organs", criticalOrgansRepository.findAll());
        model.addAttribute("chosenOrgans", "");
        return "admin";
    }

    @PostMapping(value = "/save-substance-organs-info")
    @ResponseBody
    public String saveSubstanceInfo(@RequestParam(value = "chosenOrgans") String chosenOrgans) {
        return chosenOrgans;
    }
}
