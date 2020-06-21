package com.kpi.diplom.riskassessment.service.calculator.risk;

import com.kpi.diplom.riskassessment.model.CriticalOrgan;
import com.kpi.diplom.riskassessment.model.Substance;
import com.kpi.diplom.riskassessment.model.SubstanceCriticalOrgans;
import com.kpi.diplom.riskassessment.repository.SubstanceRepository;
import com.kpi.diplom.riskassessment.service.SubstanceService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NonCarcinogenicCalculator {

    private final SubstanceRepository substanceRepository;

    private final SubstanceService substanceService;

    public String calculateRisk(String substanceName, String concentration) {
        Substance substance = substanceRepository.findByName(substanceName);
        return substance != null ? calc(substance, concentration) : "Not valid data received. Try again!";
    }

    private String calc(Substance substance, String concentration) {
        BigDecimal c = new BigDecimal(concentration);
        BigDecimal result = c.divide(substance.getRfc(), 5, RoundingMode.HALF_UP);
        return formMessage(result);
    }

    private String formMessage(BigDecimal result) {
        int resultOfComparing = result.compareTo(BigDecimal.ONE);
        switch (resultOfComparing) {
            case -1:
                return "Коефіцієнт небезпеки (HQ) = " + result.setScale(4, RoundingMode.HALF_UP).toString() +
                        "\nРизик виникнення шкідливих ефектів розглядають як зневажливо малий";
            case 0:
                return "Коефіцієнт небезпеки (HQ) = " + result.setScale(4, RoundingMode.HALF_UP).toString() +
                        "\nГранична величина, що не потребує термінових заходів, однак не може розглядатися як досить прийнятна";
            case 1:
                return "Коефіцієнт небезпеки (HQ) = " + result.setScale(4, RoundingMode.HALF_UP).toString() +
                        "\nІмовірність розвитку шкідливих ефектів зростає пропорційно збільшенню HQ";
            default:
                return "Not valid data received. Try again!";
        }
    }

    public String calculateSumRisk(List<String> substances, List<String> doses) {
        JSONObject jsonString = new JSONObject();

        List<Substance> chosenSubstances = substanceService.getChosenSubstancesById(substances);

        List<CriticalOrgan> criticalOrgans = chosenSubstances.stream()
                .map(Substance::getCriticalOrgans)
                .flatMap(Collection::stream)
                .map(SubstanceCriticalOrgans::getCriticalOrgan)
                .distinct()
                .collect(Collectors.toList());

        Map<String, BigDecimal> organsMap = criticalOrgans.stream()
                .collect(Collectors.toMap(CriticalOrgan::getName, x -> BigDecimal.ZERO));

        JSONObject hqObject = new JSONObject();
        jsonString.put("HQ", hqObject);
        BigDecimal hqGeneral = BigDecimal.ZERO;
        for (int i = 0; i < chosenSubstances.size(); i++) {
            Substance substance = chosenSubstances.get(i);
            BigDecimal dose = new BigDecimal(doses.get(i));
            BigDecimal result = dose.divide(substance.getRfd(), 5, RoundingMode.HALF_UP);
            hqObject.put("hq" + substance.getId(), result.toString());
            for (SubstanceCriticalOrgans x : substance.getCriticalOrgans()) {
                BigDecimal result1 = organsMap.get(x.getCriticalOrgan().getName()).add(result);
                organsMap.put(x.getCriticalOrgan().getName(), result1);
                hqGeneral = hqGeneral.add(result);
            }
        }
        jsonString.put("hqGeneral", hqGeneral.toString());

        JSONObject hqObjectOrgans = new JSONObject();
        jsonString.put("HQorgans", hqObjectOrgans);
        organsMap.forEach((key, value) -> hqObjectOrgans.put(key, value.toString()));

        return jsonString.toString();
    }
}
