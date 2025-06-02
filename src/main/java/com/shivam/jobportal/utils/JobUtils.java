package com.shivam.jobportal.utils;

import com.shivam.jobportal.dtos.JobResponse;
import com.shivam.jobportal.models.ExperienceLevel;
import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.models.JobType;
import com.shivam.jobportal.models.Skill;

import java.util.Arrays;

public class JobUtils {
    public static JobResponse from(Job job) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setId(job.getId());
        jobResponse.setPosition(job.getPosition());
        if (job.getSkills() != null)
            jobResponse.setSkills(job.getSkills().stream().map(Skill::getName).toList());
        jobResponse.setMinExperience(job.getMinExperience());
        jobResponse.setMaxExperience(job.getMaxExperience());
        jobResponse.setMinSalary(job.getMinSalary());
        jobResponse.setMaxSalary(job.getMaxSalary());
        jobResponse.setNoticePeriod(job.getNoticePeriod());
        jobResponse.setDescription(job.getDescription());
        jobResponse.setPostedAt(DateUtils.formatDate(job.getPostedAt()));
        jobResponse.setPostedBy(job.getPostedBy().getName());
        jobResponse.setLocation(job.getLocation());
        jobResponse.setIsRemote(job.getIsRemote());
        jobResponse.setJobType(job.getJobType().toString());
        jobResponse.setExperienceLevel(job.getExperienceLevel().toString());
        return jobResponse;
    }

    public static boolean isInvalidJobType(String jobType){
        return Arrays.stream(JobType.values())
                .map(Enum::toString)
                .noneMatch(type -> type.equals(jobType));
    }

    public static boolean isInvalidExperienceLevel(String experienceLevel) {
        return Arrays.stream(ExperienceLevel.values())
                .map(Enum::toString)
                .noneMatch(level -> level.equals(experienceLevel));
    }
}
