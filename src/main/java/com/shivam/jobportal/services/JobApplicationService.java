package com.shivam.jobportal.services;

import com.shivam.jobportal.dtos.ApplicationStatusChangeEvent;
import com.shivam.jobportal.dtos.JobApplicationEvent;
import com.shivam.jobportal.exceptions.*;
import com.shivam.jobportal.models.*;
import com.shivam.jobportal.repositories.JobApplicationRepository;
import com.shivam.jobportal.repositories.JobRepository;
import com.shivam.jobportal.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobApplicationService implements IJobApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final NotificationProducerService notificationProducerService;

    public JobApplicationService(JobApplicationRepository applicationRepository, JobRepository jobRepository,
                                 UserRepository userRepository, NotificationProducerService notificationProducerService) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.notificationProducerService = notificationProducerService;
    }

    @Override
    public JobApplication applyToJob(Long jobId, String resumeUrl, String applicantEmail) {
        Job job = jobRepository.findByIdAndState(jobId,State.ACTIVE)
                .orElseThrow(() -> new JobNotFoundException("Job not found or deleted"));

        User applicant = userRepository.findByEmailAndState(applicantEmail, State.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("User not found or deleted"));

        if (applicationRepository.existsByJobAndApplicantAndState(job, applicant, State.ACTIVE)) {
            throw new JobApplicationAlreadyExistException("You have already applied to this job");
        }

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setResumeUrl(resumeUrl);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(new Date());

        JobApplication savedApplication =  applicationRepository.save(application);

        JobApplicationEvent event = new JobApplicationEvent();
        event.setApplicantEmail(applicant.getEmail());
        event.setJobTitle(application.getJob().getPosition());
        event.setAppliedAt(application.getAppliedAt());
        notificationProducerService.sendJobApplicationEvent(event);

        return savedApplication;
    }

    @Override
    public List<JobApplication> getApplicationsForRecruiter(String recruiterEmail) {
        return applicationRepository.findByJob_PostedBy_EmailAndState(recruiterEmail,State.ACTIVE);
    }

    @Override
    public JobApplication updateApplicationStatus(Long applicationId, ApplicationStatus status, String recruiterEmail) {
        JobApplication jobApplication = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new JobApplicationNotFoundException("Job application not found or deleted"));

        if (!jobApplication.getJob().getPostedBy().getEmail().equals(recruiterEmail)) {
            throw new ForbiddenOperationException("You are not allowed to update this application");
        }

        jobApplication.setStatus(status);
        JobApplication updatedApplication =  applicationRepository.save(jobApplication);

        ApplicationStatusChangeEvent statusChangedEvent = new ApplicationStatusChangeEvent();
        statusChangedEvent.setApplicantEmail(jobApplication.getApplicant().getEmail());
        statusChangedEvent.setJobTitle(jobApplication.getJob().getPosition());
        statusChangedEvent.setNewStatus(status.name());
        statusChangedEvent.setUpdatedAt(new Date());
        notificationProducerService.sendStatusChangeEvent(statusChangedEvent);

        return updatedApplication;
    }
}
