package com.shivam.jobportal.controllers;

import com.shivam.jobportal.dtos.JobFilterRequest;
import com.shivam.jobportal.dtos.JobResponse;
import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.services.ISearchService;
import com.shivam.jobportal.utils.JobUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final ISearchService searchService;

    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<Page<JobResponse>> browseJobs(@RequestBody JobFilterRequest request) {
        Page<Job> jobs = searchService.filterJobs(request);
        Page<JobResponse> page = jobs.map(JobUtils::from);
        return ResponseEntity.ok(page);
    }
}
