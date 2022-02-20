package com.sadatmalik.fusionweb.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuickStatsController {

    @GetMapping("/quickstats")
    public String quickStats(Model model) {
        // @todo hardcoded balance
        model.addAttribute("totalBalance", String.format("£%.2f", 2247.20));
        // @todo hardcoded chart data
        model.addAttribute("chartData", getChartData());
        return "quickstats";
    }

    private List<List<Object>> getChartData() {
        return List.of(
                List.of("Date", "Balance"),
                List.of("15-Feb", 2879.32),
                List.of("16-Feb", 2700.10),
                List.of("17-Feb", 2700.10),
                List.of("18-Feb", 2247.20)
        );
    }
}
