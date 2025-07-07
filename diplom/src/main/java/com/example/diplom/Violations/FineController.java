package com.example.diplom.Violations;

import com.example.diplom.model.Driver;
import com.example.diplom.model.Vehicle;
import com.example.diplom.service.ViolationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Controller
public class FineController {
    @Autowired
    ImageService imageService;

    @Autowired
    ViolationService violationService;
    @GetMapping("/fine/{id}")
    public String viewFineDocument(@PathVariable Long id, Model model) {
        Optional<Violation> fine1 = violationService.getById(id);
        Violation fine=fine1.get();

        Driver driver=fine.getVehicle().getDriver();

        model.addAttribute("driver",driver);
        model.addAttribute("fine", fine);
        model.addAttribute("currentDate", LocalDate.now());

        // Генерация QR-кода для оплаты
        String paymentUrl = "https://www.gosuslugi.ru/pay/" + 228;
        model.addAttribute("qrCode", imageService.generateQrCode(paymentUrl));

        return "fine-prescription";
    }

}
