package com.shivam.jobportal.repositories;

import com.shivam.jobportal.models.JobBookmark;
import com.shivam.jobportal.models.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobBookmarkRepository extends JpaRepository<JobBookmark, Long> {
    List<JobBookmark> findByUserEmailAndState(String userEmail, State state);
    Optional<JobBookmark> findByUserEmailAndJobIdAndState(String userEmail, Long jobId, State state);
    boolean existsByUserEmailAndJobIdAndState(String userEmail, Long jobId, State state);
}

