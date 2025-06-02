package com.shivam.jobportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobFilterRequestDto {
    private String position;
    private List<String> skills;
    private Integer minExperience;
    private Integer maxExperience;
    private Double minSalary;
    private Double maxSalary;
    private Integer noticePeriod;
    private String location;
    private Boolean isRemote;
    private String jobType; // PART_TIME, FULL_TIME
    private String experienceLevel; // JUNIOR, MID, SENIOR
    private String sortBy; // "date" or "salary"
    private String direction; // "asc" or "desc"
    private Integer page = 0;
    private Integer size = 10;
}

