package com.malam.payroll.devometer.controller;

import com.malam.payroll.devometer.model.ProfileModel;
import com.malam.payroll.devometer.model.ProfilesResponse;
import com.malam.payroll.devometer.parser.RepositoryLogParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DevOMeterController {

    @GetMapping("/getEmployeeProfiles")
    public ResponseEntity<?> getEmployeeProfiles() {
        RepositoryLogParser parser = new RepositoryLogParser();
        List<ProfileModel> profiles = parser.parse("C:\\Users\\malam_payroll_dev\\temp\\hrm-ws\\log.csv");
        ProfilesResponse response = new ProfilesResponse(profiles);
        return ResponseEntity.ok(response);
    }
}
