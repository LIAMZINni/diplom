package com.example.diplom.controler;

import com.example.diplom.Vehicle.VehicleWantedInfo;
import com.example.diplom.model.*;
import com.example.diplom.repository.CameraReportRepository;
import com.example.diplom.repository.CameraRepository;
import com.example.diplom.repository.InspectorRepository;
import com.example.diplom.repository.ViolationRepository;
import com.example.diplom.service.CameraReportService;
import com.example.diplom.service.VehicleInfoService;
import com.example.diplom.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class VerificationController {
    @Autowired
    private  CameraReportRepository reportRepository;
    @Autowired
    private  VehicleInfoService vehicleInfoService;
    @Autowired
    private CameraReportService cameraReportService;
    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private ViolationService violationService;
    @Autowired
    ViolationRepository violationRepository;
    @Autowired
    private InspectorRepository inspectorRepository;


    @GetMapping("/reports")
    public String getReport(Model model, RedirectAttributes redirectAttributes) {
        Optional<CameraReport> report1 =violationService.findNextUnprocessedReport();
        CameraReport report=report1.get();


        Vehicle vehicleInfo = vehicleInfoService.getVehicleInfo(report.getPlateNumber());
        VehicleWantedInfo info = vehicleInfoService.getFullInfoByPlate(report.getPlateNumber());

        model.addAttribute("report", report);
        model.addAttribute("vehicle", vehicleInfo);
        model.addAttribute("vehicleInfo",info);
        if (report1.isPresent()) {
            return "redirect:/" + report1.get().getCameraReportId();
        }
        else{
            redirectAttributes.addFlashAttribute("message",
                    "Нарушение #" + " подтверждено. Неподтвержденных нарушений нет.");
        }

        return "report";
    }



    @PostMapping("/verify/{id}")
    public String processVerification(
            @PathVariable Long id,
            @ModelAttribute ("decision") String decision,
            @RequestParam(required = false) String comment,
            Principal principal,
            RedirectAttributes redirectAttributes) throws ChangeSetPersister.NotFoundException {

        Optional<CameraReport> report = Optional.ofNullable(cameraReportService.getReportById(id).orElseThrow(() -> new ChangeSetPersister.NotFoundException()));
        CameraReport cameraReport=report.get();


        Optional<CameraReport> nextReport = violationService.findNextUnprocessedReport();

        Violation violation=violationService.createFromCameraReport(cameraReport,comment,decision);



        if (nextReport.isPresent()) {
            return "redirect:/" + violationService.findNextUnprocessedReport().get().getCameraReportId();
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "Нарушение #" +violation.getId()+"" + " подтверждено. Неподтвержденных нарушений нет.");
            return "redirect:/dashboard";
        }
    }







    // Просмотр деталей нарушения
    @GetMapping("/{id}")
    public String getReportDetails(@PathVariable Long id, Model model) {
        Optional<CameraReport> report1 =reportRepository.findById(id) ;
        CameraReport report=report1.get();


        Vehicle vehicleInfo = vehicleInfoService.getVehicleInfo(report.getPlateNumber());
        VehicleWantedInfo info = vehicleInfoService.getFullInfoByPlate(report.getPlateNumber());

        model.addAttribute("report", report);
        model.addAttribute("vehicle", vehicleInfo);
        model.addAttribute("vehicleInfo",info);
        return "report";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getReportImage(@PathVariable Long id) throws Exception {
        CameraReport report = reportRepository.findById(id)
                .orElseThrow(() -> new Exception("Отчет не найден"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(report.getImageMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + report.getImageFileName() + "\"")
                .body(report.getImageData());
    }



    // Удаление отчета
    @PostMapping("/{id}/delete")
    public String deleteReport(@PathVariable Long id) {
        cameraReportService.deleteReport(id);
        return "redirect:/reports";
    }







}


