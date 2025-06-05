package com.shivam.jobportal.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationEvent {
    private String applicantEmail;
    private String jobTitle;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date appliedAt;
}
