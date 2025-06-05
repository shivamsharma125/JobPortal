package com.shivam.jobportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminOverviewResponseDto {
    private Long jobCount;
    private Long applicationCount;
    private Long newRecruiterLastMonth;
    private Long newJobSeekerLastMonth;
}
