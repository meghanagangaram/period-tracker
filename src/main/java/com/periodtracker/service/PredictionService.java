
package com.periodtracker.service;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PredictionService{

public LocalDate predictNextPeriod(LocalDate start){
return start.plusDays(30);
}

public String tips(String symptom){
if(symptom.contains("Cramps")) return "Use heating pad and drink warm water";
if(symptom.contains("Fatigue")) return "Eat iron rich foods";
if(symptom.contains("Headache")) return "Stay hydrated and rest";
if(symptom.contains("Stomach pain")) return "Eat dark chocolates";
if(symptom.contains("backpain")) return "apply heat (heating pad/warm bath)";
if(symptom.contains("Mood swings")) return "30 minutes of daily exercise,sleep well";

return "Maintain healthy lifestyle";
}
public LocalDate predictNextPeriod(List<Long> cycleLengths, LocalDate lastStartDate){

    if(cycleLengths == null || cycleLengths.size() == 0){
        return lastStartDate.plusDays(28);
    }

    long sum = 0;

    for(Long d : cycleLengths){
        sum += d;
    }

    long avg = sum / cycleLengths.size();

    return lastStartDate.plusDays(avg);
}
}
