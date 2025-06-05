package com.shivam.jobportal.services;

import com.shivam.jobportal.dtos.AdminOverviewResponseDto;
import com.shivam.jobportal.dtos.ApplicationsPerDayResponseDto;
import com.shivam.jobportal.dtos.JobsPerRecruiterResponseDto;

import java.util.List;

public interface IAdminAnalyticsService {
    AdminOverviewResponseDto getOverview();
    List<JobsPerRecruiterResponseDto> getJobsPerRecruiter();
    List<ApplicationsPerDayResponseDto> getApplicationsPerDay(int days);
}

