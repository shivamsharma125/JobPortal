package com.shivam.jobportal.services;

import com.shivam.jobportal.models.JobBookmark;

import java.util.List;

public interface IJobBookmarkService {
    JobBookmark bookmark(Long jobId, String userEmail);
    void removeBookmark(Long jobId, String userEmail);
    List<JobBookmark> getBookmarkedJobs(String userEmail);
}
