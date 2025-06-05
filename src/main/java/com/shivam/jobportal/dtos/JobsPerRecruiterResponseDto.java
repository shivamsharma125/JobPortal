package com.shivam.jobportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobsPerRecruiterResponseDto {
    private Long recruiterId;
    private String recruiterName;
    private Long jobCount;
}
