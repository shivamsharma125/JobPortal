package com.shivam.jobportal.repositories;

import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.models.JobApplication;
import com.shivam.jobportal.models.State;
import com.shivam.jobportal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {
    JobApplication job(Job job);
    boolean existsByJobAndApplicantAndState(Job job, User applicant, State state);
    List<JobApplication> findByJob_PostedBy_EmailAndState(String recruiterEmail, State state);
}
