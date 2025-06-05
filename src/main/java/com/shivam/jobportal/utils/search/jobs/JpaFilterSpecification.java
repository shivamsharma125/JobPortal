package com.shivam.jobportal.utils.search.jobs;

import com.shivam.jobportal.dtos.JobFilterRequestDto;
import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.models.Skill;
import com.shivam.jobportal.models.State;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JpaFilterSpecification {

    public static Specification<Job> filterJobs(JobFilterRequestDto request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("state"), State.ACTIVE));

            if (request.getPosition() != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("position")), "%" + request.getPosition().toLowerCase() + "%"));
            }

            if (request.getLocation() != null)
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("location")), request.getLocation().toLowerCase()));

            if (request.getIsRemote() != null)
                predicates.add(criteriaBuilder.equal(root.get("isRemote"), request.getIsRemote()));

            if (request.getJobType() != null)
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("jobType")), request.getJobType().toLowerCase()));

            if (request.getExperienceLevel() != null)
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("experienceLevel")), request.getExperienceLevel().toLowerCase()));

            if (request.getMinExperience() != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("minExperience"), request.getMinExperience()));

            if (request.getMaxExperience() != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxExperience"), request.getMaxExperience()));

            if (request.getMinSalary() != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("minSalary"), request.getMinSalary()));

            if (request.getMaxSalary() != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("maxSalary"), request.getMaxSalary()));

            if (request.getNoticePeriod() != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("noticePeriod"), request.getNoticePeriod()));

            if (request.getSkills() != null && !request.getSkills().isEmpty()) {
                Join<Job, Skill> skillJoin = root.join("skills", JoinType.INNER);
                List<String> lowercaseSkills = request.getSkills().stream()
                        .map(String::toLowerCase)
                        .toList();
                predicates.add(criteriaBuilder.lower(skillJoin.get("name")).in(lowercaseSkills));
                query.distinct(true);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

