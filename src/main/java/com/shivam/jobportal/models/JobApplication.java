package com.shivam.jobportal.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_applications",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"job_id", "applicant_id"})
})
public class JobApplication extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Column(nullable = false)
    private String resumeUrl;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(nullable = false)
    private Date appliedAt;
}

