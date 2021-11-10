package com.malam.payroll.devometer.types;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public enum Seniority {
    JUNIOR,
    MIDDLE,
    SENIOR;

    public static Seniority fromDate(Date date) {
        LocalDate startDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDate endDate = LocalDate.now();

        Period period = Period.between(startDate, endDate);
        int years = period.getYears();

        if (years >= 2) {
            return SENIOR;
        }

        if (years >= 1) {
            return MIDDLE;
        }

        return JUNIOR;
    }
}
