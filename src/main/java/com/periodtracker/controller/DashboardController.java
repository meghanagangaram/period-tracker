package com.periodtracker.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.periodtracker.model.PeriodData;
import com.periodtracker.repository.PeriodRepository;
import com.periodtracker.service.PredictionService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @Autowired
    PeriodRepository repo;

    @Autowired
    PredictionService service;

    // SAVE PERIOD + PREDICTION
    @PostMapping("/savePeriod")
    public String savePeriod(PeriodData data, Model model) {

        repo.save(data);

        // Prediction
        LocalDate next = service.predictNextPeriod(data.getStartDate());
        String tips = service.tips(data.getSymptoms());

        model.addAttribute("nextDate", next);
        model.addAttribute("tips", tips);

        // 🔥 CYCLE ANALYTICS
        List<PeriodData> periods = repo.findTop5ByOrderByStartDateDesc();
        List<Long> cycleLengths = new ArrayList<>();

        for (int i = 0; i < periods.size() - 1; i++) {
            LocalDate current = periods.get(i).getStartDate();
            LocalDate nextDate = periods.get(i + 1).getStartDate();

            long diff = ChronoUnit.DAYS.between(nextDate, current);
            cycleLengths.add(diff);
        }

        model.addAttribute("cycleData", cycleLengths);

        // 🔥 1. IRREGULAR CHECK
        boolean irregular = false;
        for(Long c : cycleLengths){
            if(c < 21 || c > 35){
                irregular = true;
                break;
            }
        }
        model.addAttribute("irregular", irregular);

        // 🔥 2. TREND DETECTION
        String trend = "Stable";
        if(cycleLengths.size() >= 2){
            long latest = cycleLengths.get(0);
            long previous = cycleLengths.get(1);

            if(latest > previous){
                trend = "Increasing";
            } else if(latest < previous){
                trend = "Decreasing";
            }
        }
        model.addAttribute("trend", trend);

        // 🔥 3. CONFIDENCE SCORE
        int confidence = 100;

        if(cycleLengths.size() > 1){
            long sum = 0;
            for(Long c : cycleLengths){
                sum += c;
            }

            long avg = sum / cycleLengths.size();

            long variance = 0;
            for(Long c : cycleLengths){
                variance += Math.abs(c - avg);
            }

            confidence = 100 - (int)(variance / cycleLengths.size());
        }
        model.addAttribute("confidence", confidence);

        // 🔥 4. MISSED CYCLE ALERT
        boolean missed = false;
        if(cycleLengths.size() > 0){
            long lastCycle = cycleLengths.get(0);
            if(lastCycle > 40){
                missed = true;
            }
        }
        model.addAttribute("missed", missed);

        return "dashboard";
    }

    // HISTORY PAGE
    @GetMapping("/history")
public String getHistory(Model model) {

    List<PeriodData> history = repo.findTop5ByOrderByStartDateDesc();

    // 🔥 DEBUG (IMPORTANT)
    System.out.println("HISTORY DATA: " + history);

    model.addAttribute("history", history);

    return "history";
}

    @GetMapping("/dashboard")
public String showDashboard(HttpSession session, Model model){

    if(session.getAttribute("userId") == null){
        return "redirect:/login";
    }

    // ✅ ADD SAFE DEFAULT DATA (THIS FIXES 500)
    model.addAttribute("cycleData", new ArrayList<>());
    model.addAttribute("irregular", false);
    model.addAttribute("trend", "Stable");
    model.addAttribute("confidence", 0);
    model.addAttribute("missed", false);

    return "dashboard";
}
}