package com.malam.payroll.devometer;

import com.malam.payroll.devometer.model.ProfileModel;
import com.malam.payroll.devometer.parser.RepositoryLogParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DevOMeterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevOMeterApplication.class, args);

        RepositoryLogParser parser = new RepositoryLogParser();
        List<ProfileModel> profiles = parser.parse("C:\\Users\\malam_payroll_dev\\temp\\hrm-ws\\log.csv");

        System.out.println(profiles);
    }

}
