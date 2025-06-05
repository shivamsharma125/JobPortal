package com.shivam.jobportal.services;

import com.shivam.jobportal.clients.ElasticSearchApiClient;
import com.shivam.jobportal.dtos.JobRequestDto;
import com.shivam.jobportal.exceptions.ForbiddenOperationException;
import com.shivam.jobportal.exceptions.InvalidRequestException;
import com.shivam.jobportal.exceptions.JobNotFoundException;
import com.shivam.jobportal.models.*;
import com.shivam.jobportal.repositories.JobRepository;
import com.shivam.jobportal.repositories.SkillRepository;
import com.shivam.jobportal.repositories.UserRepository;
import com.shivam.jobportal.utils.RequestUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.shivam.jobportal.utils.JobUtils.from;

@Service
public class JobService implements IJobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final ElasticSearchApiClient elasticSearchApiClient;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
                      UserRepository userRepository, ElasticSearchApiClient elasticSearchApiClient) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.elasticSearchApiClient = elasticSearchApiClient;
    }

    @Override
    public Job createJob(JobRequestDto jobRequest, String recruiterEmail) {
        User recruiter = userRepository.findByEmailAndState(recruiterEmail,State.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException("Recruiter not found"));

        List<Skill> skills = jobRequest.getSkills().stream()
                .map(skillName -> skillRepository.findByName(skillName)
                        .orElseGet(() -> skillRepository.save(new Skill(skillName))))
                .toList();

        Job job = new Job();
        job.setPosition(jobRequest.getPosition());
        job.setDescription(jobRequest.getDescription());
        job.setMinExperience(jobRequest.getMinExperience());
        job.setMaxExperience(jobRequest.getMaxExperience());
        job.setMinSalary(jobRequest.getMinSalary());
        job.setMaxSalary(jobRequest.getMaxSalary());
        job.setNoticePeriod(jobRequest.getNoticePeriod());
        job.setPostedAt(new Date());
        job.setSkills(skills);
        job.setPostedBy(recruiter);
        job.setLocation(jobRequest.getLocation());
        job.setIsRemote(jobRequest.getIsRemote());
        job.setJobType(JobType.valueOf(jobRequest.getJobType()));
        job.setExperienceLevel(ExperienceLevel.valueOf(jobRequest.getExperienceLevel()));;

        Job savedJob = jobRepository.save(job);

        // Index in Elasticsearch
        elasticSearchApiClient.saveJob(from(savedJob));

        return savedJob;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAllByState(State.ACTIVE);
    }

    @Override
    public List<Job> getJobsByRecruiter(String recruiterEmail) {
        User recruiter = userRepository.findByEmailAndState(recruiterEmail,State.ACTIVE)
                .orElseThrow(() -> new UsernameNotFoundException("Recruiter not found"));

        return jobRepository.findAllByPostedByIdAndState(recruiter.getId(),State.ACTIVE);
    }

    @Override
    public Job updateJob(Long jobId, JobRequestDto jobRequest, String recruiterEmail) {
        Job savedJob = jobRepository.findByIdAndState(jobId,State.ACTIVE)
                .orElseThrow(() -> new JobNotFoundException("Job does not exist"));

        if (!savedJob.getPostedBy().getEmail().equals(recruiterEmail)) {
            throw new ForbiddenOperationException("User is not the owner of this job posting.");
        }

        Integer minExperience = jobRequest.getMinExperience() != null ? jobRequest.getMinExperience() : savedJob.getMinExperience();
        Integer maxExperience = jobRequest.getMaxExperience() != null ? jobRequest.getMaxExperience() : savedJob.getMaxExperience();

        if (RequestUtils.isInvalidExperienceRange(minExperience,maxExperience))
            throw new InvalidRequestException("Invalid minExperience or maxExperience");

        Double minSalary = jobRequest.getMinSalary() != null ? jobRequest.getMinSalary() : savedJob.getMinSalary();
        Double maxSalary = jobRequest.getMaxSalary() != null ? jobRequest.getMaxSalary() : savedJob.getMaxSalary();

        if (RequestUtils.isInvalidSalaryRange(minSalary,maxSalary))
            throw new InvalidRequestException("Invalid minSalary or maxSalary");

        List<Skill> skills = jobRequest.getSkills() == null ? savedJob.getSkills() :
                jobRequest.getSkills().stream()
                        .map(skillName -> skillRepository.findByName(skillName)
                        .orElseGet(() -> skillRepository.save(new Skill(skillName))))
                        .collect(Collectors.toCollection(ArrayList::new));

        // Update only those fields which are not null
        if (jobRequest.getPosition() != null)
            savedJob.setPosition(jobRequest.getPosition());
        savedJob.setSkills(skills);
        savedJob.setMinExperience(minExperience);
        savedJob.setMaxExperience(maxExperience);
        savedJob.setMinSalary(minSalary);
        savedJob.setMaxSalary(maxSalary);
        if (jobRequest.getNoticePeriod() != null)
            savedJob.setNoticePeriod(jobRequest.getNoticePeriod());
        if (jobRequest.getDescription() != null)
            savedJob.setDescription(jobRequest.getDescription());
        if (jobRequest.getLocation() != null)
            savedJob.setLocation(jobRequest.getLocation());
        if (jobRequest.getIsRemote() != null)
            savedJob.setIsRemote(jobRequest.getIsRemote());
        if (jobRequest.getJobType() != null)
            savedJob.setJobType(JobType.valueOf(jobRequest.getJobType()));
        if (jobRequest.getExperienceLevel() != null)
            savedJob.setExperienceLevel(ExperienceLevel.valueOf(jobRequest.getExperienceLevel()));

        savedJob = jobRepository.save(savedJob);

        // Update in Elasticsearch
        elasticSearchApiClient.updateJob(from(savedJob));

        return savedJob;
    }

    @Override
    public void deleteJob(Long jobId, String recruiterEmail) {
        Job job = jobRepository.findByIdAndState(jobId,State.ACTIVE)
                .orElseThrow(() -> new JobNotFoundException("Job does not exist or deleted"));

        if (!job.getPostedBy().getEmail().equals(recruiterEmail)) {
            throw new ForbiddenOperationException("You are not the owner of this job posting.");
        }

        jobRepository.delete(job);

        // Remove from Elasticsearch
        elasticSearchApiClient.deleteJobById(jobId);
    }
}

