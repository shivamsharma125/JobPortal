package com.shivam.jobportal.services;

import com.shivam.jobportal.dtos.JobFilterRequestDto;
import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.repositories.JobRepository;
import com.shivam.jobportal.utils.specifications.JobSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SearchService implements ISearchService {
    private final JobRepository jobRepository;

    public SearchService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public Page<Job> filterJobs(JobFilterRequestDto request) {
        Sort sort = null;
        if ("salary".equalsIgnoreCase(request.getSortBy())) {
            sort = Sort.by("maxSalary");
        }
        else { // sort by date
            sort = Sort.by("postedAt");
        }

        if ("desc".equalsIgnoreCase(request.getDirection())) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        return jobRepository.findAll(JobSpecification.filterJobs(request), pageable);
    }

}
