package com.example.diplom.Analitics;

import com.example.diplom.service.CameraReportService;
import com.example.diplom.service.ViolationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnaliticsController {
    @Autowired
    ViolationService violationService;
    @Autowired
    CameraReportService cameraReportService;


    @GetMapping("/analytics")
    public String getAnalytics(Model model) {
        model.addAttribute("violationsByType", violationService.getViolationsByType());
        model.addAttribute("reportsByStatus", cameraReportService.getReportsByStatus());
        model.addAttribute("violationsByMonth", violationService.getViolationsByMonth());
        model.addAttribute("totalViolations", violationService.getTotalCount());
        model.addAttribute("totalReports", cameraReportService.getTotalCount());
        model.addAttribute("violationsByLocation", cameraReportService.getViolationsByLocation());
        model.addAttribute("hotSpots", violationService.getHotSpots());
        model.addAttribute("violationsByCamera", cameraReportService.getViolationsByCamera());
        return "analytics";
    }
}
