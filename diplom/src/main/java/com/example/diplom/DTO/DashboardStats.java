package com.example.diplom.DTO;

import lombok.Data;

@Data
public class DashboardStats {
    private long todayViolations;
    private long pendingVerifications;
    private long solvedCases;
    private long wantedVehicles;
    private long pendingAppeals;

    // Методы для вычисления процентов и т.д.
    public double getVerificationProgress() {
        return pendingVerifications == 0 ? 100 :
                (solvedCases * 100.0) / (solvedCases + pendingVerifications);
}
}
