package com.shivam.jobportal.controllers;

import com.shivam.jobportal.dtos.JobRequest;
import com.shivam.jobportal.dtos.JobResponse;
import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.models.Skill;
import com.shivam.jobportal.services.IJobService;
import com.shivam.jobportal.utils.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobController {
    private final IJobService jobService;

    public JobController(IJobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> createJob(@RequestBody JobRequest dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        Job job = jobService.createJob(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDTO(job));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('APPLICANT', 'RECRUITER')")
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        List<JobResponse> jobs = jobService.getAllJobs().stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<JobResponse>> getMyJobs(@AuthenticationPrincipal UserDetails userDetails) {
        List<JobResponse> jobs = jobService.getJobsByRecruiter(userDetails.getUsername()).stream()
                .map(this::mapToDTO)
                .toList();
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobResponse> updateJob(@PathVariable Long jobId,
                                                 @RequestBody JobRequest jobRequest,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Job job = jobService.updateJob(jobId, jobRequest, userDetails.getUsername());
        return ResponseEntity.ok(mapToDTO(job));
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        jobService.deleteJob(jobId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private JobResponse mapToDTO(Job job) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setId(job.getId());
        jobResponse.setPosition(job.getPosition());
        jobResponse.setSkills(job.getSkills().stream().map(Skill::getName).toList());
        jobResponse.setMinExperience(job.getMinExperience());
        jobResponse.setMaxExperience(job.getMaxExperience());
        jobResponse.setMinSalary(job.getMinSalary());
        jobResponse.setMaxSalary(job.getMaxSalary());
        jobResponse.setNoticePeriod(job.getNoticePeriod());
        jobResponse.setDescription(job.getDescription());
        jobResponse.setPostedAt(DateUtils.formatDate(job.getPostedAt()));
        jobResponse.setPostedBy(job.getPostedBy().getName());
        return jobResponse;
    }
}
