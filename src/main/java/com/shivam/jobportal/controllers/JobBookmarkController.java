package com.shivam.jobportal.controllers;

import com.shivam.jobportal.dtos.JobBookmarkDto;
import com.shivam.jobportal.dtos.JobDto;
import com.shivam.jobportal.exceptions.InvalidRequestException;
import com.shivam.jobportal.models.JobBookmark;
import com.shivam.jobportal.services.IJobBookmarkService;
import com.shivam.jobportal.utils.DateUtils;
import com.shivam.jobportal.utils.JobUtils;
import com.shivam.jobportal.utils.RequestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
public class JobBookmarkController {

    private final IJobBookmarkService bookmarkService;

    public JobBookmarkController(IJobBookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/{jobId}")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<JobBookmarkDto> bookmarkJob(@PathVariable Long jobId, Authentication auth) {
        if (RequestUtils.isInvalidId(jobId)) throw new InvalidRequestException("Invalid job ID");
        JobBookmark bookmark = bookmarkService.bookmark(jobId, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(from(bookmark));
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long jobId, Authentication auth) {
        if (RequestUtils.isInvalidId(jobId)) throw new InvalidRequestException("Invalid job ID");
        bookmarkService.removeBookmark(jobId,auth.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('APPLICANT')")
    public ResponseEntity<List<JobDto>> getBookmarkedJobs(Authentication auth) {
        List<JobBookmark> jobBookmarks = bookmarkService.getBookmarkedJobs(auth.getName());
        List<JobDto> jobResponses = jobBookmarks.stream()
                .map(bookmark -> JobUtils.from(bookmark.getJob()))
                .toList();
        return ResponseEntity.ok(jobResponses);
    }

    private JobBookmarkDto from(JobBookmark bookmark){
        JobBookmarkDto dto = new JobBookmarkDto();
        dto.setBookmarkId(bookmark.getId());
        dto.setJobId(bookmark.getJob().getId());
        dto.setUserId(bookmark.getUser().getId());
        dto.setBookmarkedAt(DateUtils.formatDate(bookmark.getBookmarkedAt()));
        return dto;
    }
}

