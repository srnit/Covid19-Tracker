package com.example.covid19tracker.Controller;

import com.example.covid19tracker.models.LocationStats;
import com.example.covid19tracker.services.CoronaVirusDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class Homecontroller {
    @Autowired
    CoronaVirusDataService coronaVirusDataService;
    @GetMapping("/")
     public String Home(Model model)
    {
        List<LocationStats> allStats = coronaVirusDataService.getAllstats();
        int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
        int totalNewCases = allStats.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases", totalReportedCases);
        model.addAttribute("totalNewCases", totalNewCases);

        return "Home";

    }
}
