package com.shivam.jobportal.repositories;

import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.models.JobApplication;
import com.shivam.jobportal.models.State;
import com.shivam.jobportal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {
    JobApplication job(Job job);
    boolean existsByJobAndApplicantAndState(Job job, User applicant, State state);
    List<JobApplication> findByJob_PostedBy_EmailAndState(String recruiterEmail, State state);
    long countAllByState(State state);

    @Query("SELECT ja.appliedAt, count(ja) " +
            "FROM JobApplication ja " +
            "WHERE ja.appliedAt >= :date " +
            "GROUP BY ja.appliedAt " +
            "ORDER BY ja.appliedAt DESC")
    List<Object[]> countApplicationsGroupedByDate(Date date);
}
