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
    private String position;
    @Column(length = 2000)
    private String description;
    private int minExperience;
    private int maxExperience;
    private double minSalary;
    private double maxSalary;
    private int noticePeriod;
    private Date postedAt;
    @ManyToMany
    @JoinTable(
            name = "jobs_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills; // [M:M]
    @ManyToOne
    @JoinColumn(name = "posted_by_id")
    private User postedBy;
}


