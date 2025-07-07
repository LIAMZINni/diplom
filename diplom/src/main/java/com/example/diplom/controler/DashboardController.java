package com.example.diplom.controler;

import com.example.diplom.Vehicle.repository.WantedVehicleRepository;
import com.example.diplom.Violations.ViolationRepository;
import com.example.diplom.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {
    @Autowired
    private  ViolationService violationService;

    @Autowired
    private WantedVehicleRepository wantedVehicleService;

    @Autowired
    private ViolationRepository violationRepository;

    @GetMapping("/dashboard")
    public String getDashboard(Model model, Principal principal) {
        // Основная статистика
//        DashboardStats stats = new DashboardStats();
//        stats.setTodayViolations(violationService.getTodayViolationsCount());
//        stats.setPendingVerifications(violationService.getPendingVerificationsCount());
//        stats.setSolvedCases(violationService.getSolvedCasesCount(7));
//        stats.setWantedVehicles(wantedVehicleService.findAllActive().size());
//
//
//        model.addAttribute("stats", stats);
//
//        // Последние нарушения
//        model.addAttribute("recentViolations",violationRepository.findAll()
//                );

        // Последние действия
//        model.addAttribute("recentActivities",
//                activityLogService.getRecentActivities(5));

        // Информация о пользователе
//        UserInfo user = new UserInfo();
//        user.setFullName(principal.getName()); // В реальной системе - из БД
//        user.setRole("INSPECTOR"); // В реальной системе - из БД
//
//        model.addAttribute("user", user);

        return "index";
    }

}
