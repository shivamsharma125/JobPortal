package com.shivam.jobportal.services;

import com.shivam.jobportal.dtos.JobRequestDto;
import com.shivam.jobportal.models.Job;

import java.util.List;

public interface IJobService {
    Job createJob(JobRequestDto jobRequest, String recruiterEmail);
    List<Job> getAllJobs();
    List<Job> getJobsByRecruiter(String recruiterEmail);
    Job updateJob(Long jobId, JobRequestDto jobRequest, String recruiterEmail);
    void deleteJob(Long jobId, String recruiterEmail);
}
