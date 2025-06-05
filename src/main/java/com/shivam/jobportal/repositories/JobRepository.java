package com.shivam.jobportal.repositories;

import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.models.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job,Long>, JpaSpecificationExecutor<Job> {
    List<Job> findAllByPostedByIdAndState(Long postedById, State state);
    List<Job> findAllByState(State state);
    Optional<Job> findByIdAndState(Long id, State state);
    long countAllByState(State state);
    @Query("SELECT j.postedBy.id, j.postedBy.name, count(j) " +
            "FROM Job j " +
            "JOIN j.postedBy pb " +
            "JOIN pb.roles r " +
            "WHERE j.state = :state AND r.name = 'RECRUITER' " +
            "GROUP BY j.postedBy.id, j.postedBy.name ")
    List<Object[]> countRecruiterGroupedByState(State state);
}
