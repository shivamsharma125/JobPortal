package com.shivam.jobportal.services;

import com.shivam.jobportal.models.ApplicationStatus;
import com.shivam.jobportal.models.JobApplication;

import java.util.List;

public interface IJobApplicationService {
    JobApplication applyToJob(Long jobId, String resumeUrl, String applicantEmail);
    List<JobApplication> getApplicationsForRecruiter(String recruiterEmail);
    JobApplication updateApplicationStatus(Long applicationId, ApplicationStatus status, String recruiterEmail);
}
