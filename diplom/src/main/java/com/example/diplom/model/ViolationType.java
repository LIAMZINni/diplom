package com.example.diplom.model;

import com.example.diplom.Violations.Violation;
import com.example.diplom.Violations.ViolationStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;


public enum ViolationType {
    SPEEDING(
            "Превышение скорости",
            "Статья 12.9 КоАП РФ",
            violation -> {
                int excess = violation.getCameraReport().getDetectedSpeed() - violation.getCameraReport().getSpeedLimit();
                if (excess > 60) return applyDiscount(violation, 5000);
                if (excess > 40) return applyDiscount(violation, 1500);
                if (excess > 20) return applyDiscount(violation, 500);
                return 0.0; // Если превышение менее 20 км/ч - штрафа нет
            }
    ),
    RED_LIGHT(
            "Проезд на красный",
            "Статья 12.12 КоАП РФ",
            violation -> applyDiscount(violation, 1000)
    ),
    NO_SEATBELT(
            "Непристёгнутый ремень",
            "Статья 12.6 КоАП РФ",
            violation -> applyDiscount(violation, 1000)
    ),
    PHONE_WHILE_DRIVING(
            "Использование телефона во время движения",
            "Статья 12.36.1 КоАП РФ",
            violation -> applyDiscount(violation, 1500)
    ),
    WRONG_WAY(
            "Движение во встречном направлении",
            "Статья 12.15 ч.4 КоАП РФ",
            violation -> applyDiscount(violation, 5000)
    ),
    NO_STOPS(
            "Неостановка перед стоп-линией",
            "Статья 12.12 ч.2 КоАП РФ",
            violation -> applyDiscount(violation, 800)
    ),
    DIRTY_PLATES(
            "Грязные или нечитаемые номера",
            "Статья 12.2 ч.1 КоАП РФ",
            violation -> applyDiscount(violation, 500)
    ),
    PARKING(
            "Неправильная парковка",
            "Статья 12.19 КоАП РФ",
            violation -> applyDiscount(violation, 500)
    );

    private final String displayName;
    private final String article;
    private final Function<Violation, Double> fineCalculator;

    ViolationType(String description, String article, Function<Violation, Double> fineCalculator) {
        this.displayName = description;
        this.article = article;
        this.fineCalculator = fineCalculator;
    }

    public Double calculateFine(Violation violation) {
        return fineCalculator.apply(violation);
    }
    private static double applyDiscount(Violation violation, double baseFine) {
        if (violation.getStatus() == ViolationStatus.PAID) {
            return baseFine; // Уже оплачен - скидка не применяется
        }

        long daysSinceCreation = ChronoUnit.DAYS.between(
                violation.getViolationTime(),
                LocalDateTime.now()
        );

        if (daysSinceCreation <= 30) {
            return baseFine * 0.75; // 50% скидка
        }
        return baseFine;
    }

    // Геттеры
    public String getDisplayName() { return displayName; }
    public String getArticle() { return article; }
    public double getBaseAmount() {
        switch(this) {
            case SPEEDING: return 500; // Минимальная базовая сумма
            case RED_LIGHT: return 1000;
            case NO_SEATBELT: return 1000;
            case PHONE_WHILE_DRIVING: return 1500;
            case WRONG_WAY: return 5000;
            case NO_STOPS: return 800;
            case DIRTY_PLATES: return 500;
            case PARKING: return 500;
            default: return 0;
        }
    }
}

