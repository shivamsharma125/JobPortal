package com.shivam.jobportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jobs")
public class Job extends BaseModel {
    @Column(nullable = false)
    private String position;
    @Column(length = 2000, nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer minExperience;
    @Column(nullable = false)
    private Integer maxExperience;
    @Column(nullable = false)
    private Double minSalary;
    @Column(nullable = false)
    private Double maxSalary;
    @Column(nullable = false)
    private Integer noticePeriod;
    @Column(nullable = false)
    private Date postedAt;
    @ManyToMany
    @JoinTable(
            name = "jobs_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @Column(nullable = false)
    private List<Skill> skills; // [M:M]
    @ManyToOne
    @JoinColumn(name = "posted_by_id", nullable = false)
    private User postedBy;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private Boolean isRemote;
    @Column(nullable = false)
    private JobType jobType;
    @Column(nullable = false)
    private ExperienceLevel experienceLevel;
}
