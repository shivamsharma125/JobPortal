package com.shivam.jobportal.controllers;

import com.shivam.jobportal.dtos.AdminOverviewResponseDto;
import com.shivam.jobportal.dtos.ApplicationsPerDayResponseDto;
import com.shivam.jobportal.dtos.JobsPerRecruiterResponseDto;
import com.shivam.jobportal.exceptions.InvalidRequestException;
import com.shivam.jobportal.services.IAdminAnalyticsService;
import com.shivam.jobportal.utils.RequestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/analytics")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnalyticsController {

    private final IAdminAnalyticsService adminAnalyticsService;

    public AdminAnalyticsController(IAdminAnalyticsService adminAnalyticsService) {
        this.adminAnalyticsService = adminAnalyticsService;
    }

    @GetMapping("/overview")
    public ResponseEntity<AdminOverviewResponseDto> getOverview() {
        return ResponseEntity.ok(adminAnalyticsService.getOverview());
    }

    @GetMapping("/jobs-per-recruiter")
    public ResponseEntity<List<JobsPerRecruiterResponseDto>> getJobsPerRecruiter() {
        return ResponseEntity.ok(adminAnalyticsService.getJobsPerRecruiter());
    }

    @GetMapping("/applications-per-day")
    public ResponseEntity<List<ApplicationsPerDayResponseDto>> getApplicationsPerDay(
            @RequestParam(defaultValue = "7") int days) {
        if (RequestUtils.isInvalidNumberOfDays(days)) throw new InvalidRequestException("Invalid count of days");

        return ResponseEntity.ok(adminAnalyticsService.getApplicationsPerDay(days));
    }
}

