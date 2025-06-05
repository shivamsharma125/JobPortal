package com.shivam.jobportal.controllers;

import com.shivam.jobportal.dtos.JobDto;
import com.shivam.jobportal.dtos.JobFilterRequestDto;
import com.shivam.jobportal.services.ISearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private final ISearchService searchService;

    public SearchController(ISearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<List<JobDto>> browseJobs(@RequestBody JobFilterRequestDto request) {
        return ResponseEntity.ok(searchService.filterJobs(request));
    }
}
