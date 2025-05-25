package com.shivam.jobportal.services;

import com.shivam.jobportal.dtos.JobRequest;
import com.shivam.jobportal.exceptions.ForbiddenOperationException;
import com.shivam.jobportal.exceptions.JobNotFoundException;
import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.models.Skill;
import com.shivam.jobportal.models.User;
import com.shivam.jobportal.repositories.JobRepository;
import com.shivam.jobportal.repositories.SkillRepository;
import com.shivam.jobportal.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class JobService implements IJobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
                      UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Job createJob(JobRequest jobRequest, String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Recruiter not found"));

        List<Skill> skills = jobRequest.getSkills().stream()
                .map(skillName -> skillRepository.findByName(skillName)
                        .orElseGet(() -> skillRepository.save(new Skill(skillName))))
                .toList();

        Job job = new Job();
        job.setPosition(jobRequest.getPosition());
        job.setSkills(new ArrayList<>(skills));
        job.setMinExperience(jobRequest.getMinExperience());
        job.setMaxExperience(jobRequest.getMaxExperience());
        job.setMinSalary(jobRequest.getMinSalary());
        job.setMaxSalary(jobRequest.getMaxSalary());
        job.setNoticePeriod(jobRequest.getNoticePeriod());
        job.setDescription(jobRequest.getDescription());
        job.setPostedAt(new Date());
        job.setPostedBy(recruiter);


        return jobRepository.save(job);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public List<Job> getJobsByRecruiter(String recruiterEmail) {
        User recruiter = userRepository.findByEmail(recruiterEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Recruiter not found"));

        return jobRepository.findAllByPostedById(recruiter.getId());
    }

    @Override
    public Job updateJob(Long jobId, JobRequest jobRequest, String recruiterEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job does not exist"));

        if (!job.getPostedBy().getEmail().equals(recruiterEmail)) {
            throw new ForbiddenOperationException("User is not the owner of this job posting.");
        }

        List<Skill> skills = jobRequest.getSkills().stream()
                .map(skillName -> skillRepository.findByName(skillName)
                        .orElseGet(() -> skillRepository.save(new Skill(skillName))))
                .toList();

        job.setPosition(jobRequest.getPosition());
        job.setSkills(new ArrayList<>(skills));
        job.setMinExperience(jobRequest.getMinExperience());
        job.setMaxExperience(jobRequest.getMaxExperience());
        job.setMinSalary(jobRequest.getMinSalary());
        job.setMaxSalary(jobRequest.getMaxSalary());
        job.setNoticePeriod(jobRequest.getNoticePeriod());
        job.setDescription(jobRequest.getDescription());

        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long jobId, String recruiterEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job does not exist"));

        if (!job.getPostedBy().getEmail().equals(recruiterEmail)) {
            throw new ForbiddenOperationException("You are not the owner of this job posting.");
        }

        jobRepository.delete(job);
    }
}

