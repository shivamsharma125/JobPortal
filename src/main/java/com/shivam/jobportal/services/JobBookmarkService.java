package com.shivam.jobportal.services;

import com.shivam.jobportal.exceptions.*;
import com.shivam.jobportal.models.Job;
import com.shivam.jobportal.models.JobBookmark;
import com.shivam.jobportal.models.State;
import com.shivam.jobportal.models.User;
import com.shivam.jobportal.repositories.JobBookmarkRepository;
import com.shivam.jobportal.repositories.JobRepository;
import com.shivam.jobportal.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobBookmarkService implements IJobBookmarkService {

    private final JobBookmarkRepository bookmarkRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobBookmarkService(JobBookmarkRepository bookmarkRepository, JobRepository jobRepository,
                              UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    @Override
    public JobBookmark bookmark(Long jobId, String userEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));

        User user = userRepository.findByEmailAndState(userEmail, State.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (bookmarkRepository.existsByUserEmailAndJobIdAndState(userEmail, jobId, State.ACTIVE)) {
            throw new BookmarkAlreadyExistException("Already bookmarked");
        }

        JobBookmark bookmark = new JobBookmark(job,new Date(),user);
        return bookmarkRepository.save(bookmark);
    }

    @Override
    public void removeBookmark(Long jobId, String userEmail) {
        JobBookmark jobBookmark = bookmarkRepository.findByUserEmailAndJobIdAndState(userEmail, jobId, State.ACTIVE)
                .orElseThrow(() -> new BookmarkNotFoundException("Bookmark not found or already deleted"));

        jobBookmark.setState(State.DELETED);

        bookmarkRepository.save(jobBookmark);
    }

    @Override
    public List<JobBookmark> getBookmarkedJobs(String userEmail) {
        return bookmarkRepository.findByUserEmailAndState(userEmail,State.ACTIVE);
    }
}

