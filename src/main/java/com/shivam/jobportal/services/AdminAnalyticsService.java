package com.shivam.jobportal.services;

import com.shivam.jobportal.dtos.AdminOverviewResponseDto;
import com.shivam.jobportal.dtos.ApplicationsPerDayResponseDto;
import com.shivam.jobportal.dtos.JobsPerRecruiterResponseDto;
import com.shivam.jobportal.models.State;
import com.shivam.jobportal.models.User;
import com.shivam.jobportal.repositories.JobApplicationRepository;
import com.shivam.jobportal.repositories.JobRepository;
import com.shivam.jobportal.repositories.UserRepository;
import com.shivam.jobportal.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdminAnalyticsService implements IAdminAnalyticsService {

    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public AdminAnalyticsService(JobRepository jobRepository,
                                 JobApplicationRepository applicationRepository,
                                 UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AdminOverviewResponseDto getOverview() {
        long totalJobs = jobRepository.countAllByState(State.ACTIVE);
        long totalApplications = applicationRepository.countAllByState(State.ACTIVE);

        Date thirtyDaysAgo = DateUtils.getDateFromNow(-30);

        List<User> users = userRepository.findAllByCreatedAtAfterAndState(thirtyDaysAgo, State.ACTIVE);

        long newRecruiterLast30Days = users.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("RECRUITER"))
                )
                .toList().size();

        long newApplicantLast30Days = users.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("APPLICANT"))
                )
                .toList().size();

        return new AdminOverviewResponseDto(totalJobs, totalApplications, newRecruiterLast30Days, newApplicantLast30Days);
    }

    @Override
    public List<JobsPerRecruiterResponseDto> getJobsPerRecruiter() {
        List<Object[]> jobs = jobRepository.countRecruiterGroupedByState(State.ACTIVE);
        return jobs.stream()
                .map(obj -> new JobsPerRecruiterResponseDto(
                        (Long) obj[0], // recruiterId
                        (String) obj[1], // recruiterName
                        ((Number) obj[2]).longValue() // jobCount
                ))
                .toList();
    }

    @Override
    public List<ApplicationsPerDayResponseDto> getApplicationsPerDay(int days) {
        Date fromDate = DateUtils.getDateFromNow(-days);
        List<Object[]> applications = applicationRepository.countApplicationsGroupedByDate(fromDate);
        return applications.stream()
                .map(obj -> new ApplicationsPerDayResponseDto(
                        (Date) obj[0], // date
                        ((Number) obj[1]).longValue() // count
                ))
                .toList();
    }
}

