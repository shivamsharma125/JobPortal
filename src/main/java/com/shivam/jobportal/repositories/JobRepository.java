package com.shivam.jobportal.repositories;

import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.models.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Long>, JpaSpecificationExecutor<Job> {
    List<Job> findAllByPostedByIdAndState(Long postedById, State state);
    List<Job> findAllByState(State state);
    Optional<Job> findByIdAndState(Long id, State state);
}
