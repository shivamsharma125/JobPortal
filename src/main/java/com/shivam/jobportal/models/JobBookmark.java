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
@Table(name = "jobs_bookmarks",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "job_id"})
})
public class JobBookmark extends BaseModel {
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false)
    private Date bookmarkedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

