package com.malam.payroll.devometer.model;

import com.malam.payroll.devometer.types.ProgrammingLanguage;
import com.malam.payroll.devometer.types.Seniority;

public record ProfileModel(String author, Seniority seniority, ProgrammingLanguage language) {
}
