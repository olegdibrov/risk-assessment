package com.kpi.diplom.riskassessment.service.calculator.risk;

import com.kpi.diplom.riskassessment.model.CancerogenSubstance;
import com.kpi.diplom.riskassessment.model.ExposureFactor;
import com.kpi.diplom.riskassessment.repository.CancerogenSubstanceRepository;
import com.kpi.diplom.riskassessment.repository.ExposureFactorRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Range;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class AverageDailyDoseCalculator {

    Range<BigDecimal> high = Range.between(new BigDecimal("0.001"), new BigDecimal("100000000"));
    Range<BigDecimal> medium = Range.between(new BigDecimal("0.0001"), new BigDecimal("0.001"));
    Range<BigDecimal> low = Range.between(new BigDecimal("0.000001"), new BigDecimal("0.0001"));
    Range<BigDecimal> min = Range.between(new BigDecimal("0.000000001"), new BigDecimal("0.000001"));

    private final ExposureFactorRepository exposureFactorRepository;

    private final CancerogenSubstanceRepository cancerogenSubstanceRepository;


    public String calcAverageDailyDose(String substance, String concentration, String numOfPeoples) {
        ExposureFactor exposureFactor = exposureFactorRepository.findById(1).get();
        BigDecimal conc = new BigDecimal(concentration);

        BigDecimal left = conc.multiply(exposureFactor.getTimeOutdoors()).multiply(exposureFactor.getInhalationSpeedOutdoor());
        BigDecimal right = conc.multiply(exposureFactor.getTimeIndoors()).multiply(exposureFactor.getInhalationSpeedIndoor());
        BigDecimal adding = left.add(right);
        BigDecimal multiply = adding.multiply(exposureFactor.getFrequencyOfExposure()).multiply(exposureFactor.getDurationOfExposure());
        BigDecimal bottom = exposureFactor.getBodyWeight().multiply(exposureFactor.getExposureAveragingPeriod()).multiply(BigDecimal.valueOf(365));
        BigDecimal result = multiply.divide(bottom, 15, RoundingMode.HALF_UP);
        CancerogenSubstance cancerogenSubstance = cancerogenSubstanceRepository.findByName(substance);
        BigDecimal cr = result.multiply(cancerogenSubstance.getSf());
        BigDecimal pcr = cr.multiply(new BigDecimal(numOfPeoples));

        return formMessage(cr, pcr);
    }

    private String formMessage(BigDecimal result, BigDecimal pcr) {
        if (high.contains(result)) {
            return  "Величина індивідуального ризику: " + result.toString() + "\n. Величина популяційного ризику: " + pcr.toString() + "\n. Класифікація рівня ризику - Високий (De Manifestis) – не прийнятний для виробничих умов і населення. Необхідне здійснення заходів з усунення або зниження ризику";
        } else {
            if (medium.contains(result)) {
                return "Величина індивідуального ризику: " + result.toString() + "\n. Величина популяційного ризику: " + pcr.toString() +  "\n. Класифікація рівня ризику - Середній – припустимий для виробничих умов; за впливу на все населення необхідний динамічний контроль і поглиблене вивчення джерел і можливих наслідків шкідливих впливів для вирішення питання про заходи з управління ризиком";
            } else {
                if (low.contains(result)) {
                    return "Величина індивідуального ризику: " + result.toString() + "\n. Величина популяційного ризику: " + pcr.toString() +  "\n. Класифікація рівня ризику - Низький – припустимий ризик (рівень, на якому, як правило, встановлюються гігієнічні нормативи для населення)";
                } else {
                    return"Величина індивідуального ризику: " + result.toString() + "\n. Величина популяційного ризику: " + pcr.toString() +  "\n. Класифікація рівня ризику - Мінімальний (De Minimis) – бажана (цільова) величина ризику при проведенні оздоровчих  і природоохоронних заходів";
                }
            }
        }
    }
}
