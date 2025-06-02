package com.shivam.jobportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDto {
    private Long jobApplicationId;
    private Long jobId;
    private String jobTitle;
    private String recruiterEmail;
    private String applicantEmail;
    private String resumeUrl;
    private String status;
    private String appliedAt;
}
