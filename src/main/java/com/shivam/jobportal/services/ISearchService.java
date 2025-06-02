package com.shivam.jobportal.services;

import com.shivam.jobportal.dtos.JobFilterRequestDto;
import com.shivam.jobportal.models.Job;
import org.springframework.data.domain.Page;

public interface ISearchService {
    Page<Job> filterJobs(JobFilterRequestDto jobFilterRequest);
}
