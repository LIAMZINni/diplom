package com.example.diplom.Violations;

import com.example.diplom.repository.InspectorRepository;
import com.example.diplom.service.ViolationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class ViolationController {
    @Autowired
    private ViolationService violationService;
    @Autowired
    InspectorRepository inspectorRepository;
    @Autowired
    ViolationRepository violationRepository;

    @GetMapping("/violations")
    public String getFines(
            @RequestParam(required = false) ViolationStatus status,
            @RequestParam(required = false) String plateNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "violationTime,desc") String sort,
            Model model) {

        Page<Violation> fines = violationService.getFines(status, plateNumber, startDate, endDate, page, sort);

        model.addAttribute("fines", fines);
        model.addAttribute("statuses", ViolationStatus.values());
        model.addAttribute("inspectors",inspectorRepository.findAll() );
        return "violations";
    }

    @GetMapping("/{id}/pdf")
    public void downloadPrescription(@PathVariable Long id, HttpServletResponse response) throws Exception {
        byte[] pdf = violationService.generatePrescriptionPdf(id);

//        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=prescription_" + id + ".pdf");
        response.getOutputStream().write(pdf);
    }





//    @GetMapping("/{id}")
//    public String getFineDetails(@PathVariable Long id, Model model) {
//        Violation fine = violationService.getById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Штраф не найден"));
//        model.addAttribute("fine", fine);
//        return "fines/details";
//    }
//
//    @PostMapping("/{id}/status")
//    public String updateStatus(
//            @PathVariable Long id,
//            @RequestParam ViolationStatus status,
//            RedirectAttributes redirectAttributes) {
//
//        violationService.updateStatus(id, status);
//        redirectAttributes.addFlashAttribute("success", "Статус штрафа обновлен");
//        return "redirect:/fines/" + id;
//    }
}
