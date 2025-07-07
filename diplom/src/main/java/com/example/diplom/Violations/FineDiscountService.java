package com.example.diplom.Violations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class FineDiscountService {
    @Autowired
    private  ViolationRepository violationRepository;

    public DiscountInfo getDiscountInfo(Violation violation) {
        if (violation.getStatus() != ViolationStatus.PENDING) {
            return new DiscountInfo(0, null, violation.getType().getBaseAmount());
        }

        long daysPassed = ChronoUnit.DAYS.between(
                violation.getViolationTime(),
                LocalDateTime.now()
        );

        int discountPercent = 0;
        LocalDateTime discountDeadline = null;
        double originalAmount = violation.getType().getBaseAmount();

        if (daysPassed <= 20) {
            discountPercent = 50;
            discountDeadline = violation.getViolationTime().plusDays(20);
        } else if (daysPassed <= 30) {
            discountPercent = 25;
            discountDeadline = violation.getViolationTime().plusDays(30);
        }

        return new DiscountInfo(
                discountPercent,
                discountDeadline,
                originalAmount,
                originalAmount * (100 - discountPercent) / 100
        );
    }

    @Getter
    @AllArgsConstructor
    public static class DiscountInfo {
        private int discountPercent;
        private LocalDateTime discountDeadline;
        private double originalAmount;
        private double discountedAmount;


        public DiscountInfo(int discountPercent, Object discountDeadline, double baseAmount) {
        }
    }
}
