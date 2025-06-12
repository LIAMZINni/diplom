package com.example.diplom.controler;

import com.example.diplom.model.CameraReport;
import com.example.diplom.model.ViolationType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CreateViolationController {

    //Форма создания нового отчета
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("report", new CameraReport());
        model.addAttribute("violationTypes", ViolationType.values());
        return "form";
    }


}
