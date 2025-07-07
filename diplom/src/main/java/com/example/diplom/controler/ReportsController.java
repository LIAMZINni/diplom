package com.example.diplom.controler;

import com.example.diplom.model.CameraReport;
import com.example.diplom.model.CameraReportStatus;
import com.example.diplom.repository.CameraRepository;
import com.example.diplom.service.CameraReportService;
import com.example.diplom.service.VehicleInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportsController {
    @Autowired
    CameraReportService cameraReportService;

    @Autowired
    CameraRepository cameraRepository;


    @GetMapping("/list")
    public String listReports(
            @RequestParam(required = false) Long cameraId,
            @RequestParam(required = false) String cameraModel,
            @RequestParam(required = false) String plateNumber,
            HttpServletRequest request,
            @RequestParam(required = false) CameraReportStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "timestamp,desc") String[] sort,
            Model model) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by(parseSort(sort).toList()));
        Page<CameraReport> reports = cameraReportService.getFilteredReports(
                cameraId, cameraModel, plateNumber, status, startDate, endDate, pageable);
        model.addAttribute("request", request);
        model.addAttribute("reports", reports);
        model.addAttribute("statuses", CameraReportStatus.values());
        model.addAttribute("cameras", cameraRepository.findAll()); // Список всех камер для select
        return "list";
    }
    private Sort parseSort(String[] sort) {
        if (sort == null || sort.length == 0) {
            return Sort.by(Sort.Direction.DESC, "timestamp");
        }

        String sortField = sort[0];
        if (sortField.contains(",")) {
            String[] parts = sortField.split(",");
            String field = parts[0];
            Sort.Direction direction = parts[1].equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            return Sort.by(direction, field);
        }

        return Sort.by(Sort.Direction.DESC, sortField);
    }
}
