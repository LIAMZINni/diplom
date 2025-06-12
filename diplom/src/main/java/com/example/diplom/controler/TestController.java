package com.example.diplom.controler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {
    @Autowired
    private Test uploader;

    @GetMapping("/test-upload")
    public String testUpload() throws IOException {
        try {
            uploader.uploadManually();
            return "Тестовая загрузка выполнена успешно";
        } catch (Exception e) {
            return "Ошибка при тестовой загрузке: " + e.getMessage();
        }
    }

}