package com.shivam.jobportal.controllers;

import com.shivam.jobportal.dtos.JobApplicationDto;
import com.shivam.jobportal.dtos.JobApplicationRequestDto;
import com.shivam.jobportal.models.ApplicationStatus;
import com.shivam.jobportal.models.JobApplication;
import com.shivam.jobportal.services.IJobApplicationService;
import com.shivam.jobportal.utils.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job/applications")
public class JobApplicationController {

    private final IJobApplicationService jobApplicationService;

    public JobApplicationController(IJobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<JobApplicationDto> apply(@RequestBody JobApplicationRequestDto request,
                                                   Authentication auth) {
        JobApplication jobApplication = jobApplicationService.applyToJob(
                request.getJobId(),
                request.getResumeUrl(),
                auth.getName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(from(jobApplication));
    }

    @GetMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<JobApplicationDto>> getApplications(Authentication auth) {
        List<JobApplicationDto> jobApplications = jobApplicationService.getApplicationsForRecruiter(auth.getName())
                .stream()
                .map(this::from)
                .toList();
        return ResponseEntity.ok(jobApplications);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobApplicationDto> updateStatus(@PathVariable("id") Long applicationId,
                                                            @RequestParam ApplicationStatus status,
                                                            Authentication auth) {
        JobApplication updatedJobApplication = jobApplicationService.updateApplicationStatus(applicationId, status, auth.getName());
        return ResponseEntity.ok(from(updatedJobApplication));
    }

    private JobApplicationDto from(JobApplication jobApplication) {
        return new JobApplicationDto(
                jobApplication.getId(),
                jobApplication.getJob().getId(),
                jobApplication.getJob().getPosition(),
                jobApplication.getJob().getPostedBy().getEmail(),
                jobApplication.getApplicant().getEmail(),
                jobApplication.getResumeUrl(),
                jobApplication.getStatus().name(),
                DateUtils.formatDate(jobApplication.getAppliedAt())
        );
    }
}

