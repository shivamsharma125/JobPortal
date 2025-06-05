package com.shivam.jobportal.services;

import com.shivam.jobportal.dtos.JobDto;
import com.shivam.jobportal.dtos.JobFilterRequestDto;

import java.util.List;

public interface ISearchService {
    List<JobDto> filterJobs(JobFilterRequestDto jobFilterRequest);
}
