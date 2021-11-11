package com.malam.payroll.devometer.model;

import com.malam.payroll.devometer.types.ProgrammingLanguage;
import com.malam.payroll.devometer.types.Seniority;

import java.util.List;

public record ProfileModel(String author, Seniority seniority, ProgrammingLanguage language, List<String> teamMembers) {
}
