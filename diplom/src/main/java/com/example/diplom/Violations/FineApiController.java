package com.example.diplom.Violations;

import com.example.diplom.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineApiController {
    private final ViolationService violationService;

    @GetMapping("/{id}")
    public ViolationDetailsDto getFineDetails(@PathVariable Long id) {
        Optional<Violation> v1=violationService.getById(id);
        Violation violation=v1.get();

        return ViolationDetailsDto.builder()
                .id(violation.getId())
                .violationTime(violation.getViolationTime())
                .fineAmount((int) violation.getFineAmount())

                .vehicle(violation.getVehicle())
                .cameraReport(violation.getCameraReport())
                .inspector(violation.getInspector())
                .build();
    }




    private final ViolationRepository violationRepository;
    private final FineDiscountService discountService;

    @GetMapping("/{id}/discount-info")
    public FineDiscountService.DiscountInfo getDiscountInfo(@PathVariable Long id) throws Exception {
        Violation violation = violationRepository.findById(id)
                .orElseThrow(() -> new Exception("Штраф не найден"));
        return discountService.getDiscountInfo(violation);
    }
}