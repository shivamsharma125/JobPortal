package com.shivam.jobportal.repositories;

import com.shivam.jobportal.models.State;
import com.shivam.jobportal.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndState(String email, State state);
    boolean existsByEmailAndState(String email, State state);
    List<User> findAllByCreatedAtAfterAndState(Date createdAtAfter, State state);
}

