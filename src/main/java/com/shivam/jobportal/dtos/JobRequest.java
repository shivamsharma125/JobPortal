package com.shivam.jobportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRequest {
    private String position;
    private List<String> skills;
    private int minExperience;
    private int maxExperience;
    private double minSalary;
    private double maxSalary;
    private int noticePeriod;
    private String description;
}
