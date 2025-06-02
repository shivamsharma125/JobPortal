package com.shivam.jobportal.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobBookmarkDto {
    private Long bookmarkId;
    private Long jobId;
    private Long userId;
    private String bookmarkedAt;
}
