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
public class ApplicationsPerDayResponseDto {
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;
    private Long applicationsCount;
}
