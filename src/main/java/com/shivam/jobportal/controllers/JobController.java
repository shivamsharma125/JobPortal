package com.shivam.jobportal.controllers;

import com.shivam.jobportal.dtos.JobRequestDto;
import com.shivam.jobportal.dtos.JobDto;
import com.shivam.jobportal.exceptions.InvalidRequestException;
import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.services.IJobService;
import com.shivam.jobportal.utils.JobUtils;
import com.shivam.jobportal.utils.RequestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.shivam.jobportal.utils.JobUtils.*;

@RestController
@RequestMapping("/job")
public class JobController {
    private final IJobService jobService;

    public JobController(IJobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobDto> createJob(@RequestBody JobRequestDto request,
                                            Authentication authentication) {
        validateJobRequest(request);

        Job job = jobService.createJob(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(from(job));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('APPLICANT', 'RECRUITER')")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        List<JobDto> jobs = jobService.getAllJobs().stream()
                .map(JobUtils::from)
                .toList();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<JobDto>> getMyJobs(Authentication authentication) {
        List<JobDto> jobs = jobService.getJobsByRecruiter(authentication.getName()).stream()
                .map(JobUtils::from)
                .toList();
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<JobDto> updateJob(@PathVariable Long jobId,
                                            @RequestBody JobRequestDto jobRequest,
                                            Authentication authentication) {
        if (RequestUtils.isInvalidId(jobId)) throw new InvalidRequestException("Invalid id");
        if (RequestUtils.isInvalidNoticePeriod(jobRequest.getNoticePeriod())) throw new InvalidRequestException("Invalid or no notice period provided");
        if (JobUtils.isInvalidJobType(jobRequest.getJobType())) throw new InvalidRequestException("Invalid Job Type");
        if (JobUtils.isInvalidExperienceLevel(jobRequest.getExperienceLevel())) throw new InvalidRequestException("Invalid Experience Level");

        Job job = jobService.updateJob(jobId, jobRequest,authentication.getName());
        return ResponseEntity.ok(from(job));
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId,
                                         Authentication authentication) {
        if (RequestUtils.isInvalidId(jobId)) throw new InvalidRequestException("Invalid id");
        jobService.deleteJob(jobId, authentication.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private void validateJobRequest(JobRequestDto request) {
        if (RequestUtils.isNull(request.getPosition())) throw new InvalidRequestException("Position cannot be empty");
        if (RequestUtils.isEmptyParam(request.getSkills())) throw new InvalidRequestException("Skills cannot be empty");
        if (RequestUtils.isInvalidExperience(request.getMinExperience())) throw new InvalidRequestException("no min experience provided");
        if (RequestUtils.isInvalidExperience(request.getMaxExperience())) throw new InvalidRequestException("no max experience provided");
        if (RequestUtils.isInvalidExperienceRange(request.getMinExperience(),request.getMaxExperience())) throw new InvalidRequestException("Invalid experience range");
        if (RequestUtils.isInvalidSalary(request.getMinSalary())) throw new InvalidRequestException("no min salary provided");
        if (RequestUtils.isInvalidSalary(request.getMaxSalary())) throw new InvalidRequestException("no max salary provided");
        if (RequestUtils.isInvalidSalaryRange(request.getMinSalary(),request.getMaxSalary())) throw new InvalidRequestException("Invalid salary range");
        if (RequestUtils.isInvalidNoticePeriod(request.getNoticePeriod())) throw new InvalidRequestException("Invalid or no notice period provided");
        if (RequestUtils.isEmptyParam(request.getDescription())) throw new InvalidRequestException("job description cannot be empty");
        if (RequestUtils.isEmptyParam(request.getLocation())) throw new InvalidRequestException("location cannot be empty");
        if (RequestUtils.isNull(request.getIsRemote())) throw new InvalidRequestException("not provided remote job status");
        if (JobUtils.isInvalidJobType(request.getJobType())) throw new InvalidRequestException("Invalid Job Type");
        if (JobUtils.isInvalidExperienceLevel(request.getExperienceLevel())) throw new InvalidRequestException("Invalid Experience Level");
    }
}
