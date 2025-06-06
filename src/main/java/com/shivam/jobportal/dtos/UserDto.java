package com.shivam.jobportal.dtos;

import com.shivam.jobportal.models.RoleDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private Set<RoleDto> roles;
}