package com.example.diplom.Violations;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class PdfGenerator {
    @Autowired
    private  TemplateEngine templateEngine;

    public byte[] generatePrescription(Violation violation) throws IOException {
        Context context = new Context();
        context.setVariable("fine", violation);

        context.setVariable("now", LocalDateTime.now());

        String html = templateEngine.process("fine-prescription", context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
