package com.shivam.jobportal.security.models;

import com.shivam.jobportal.models.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomGrantedAuthority implements GrantedAuthority {
    private final Role role;

    public CustomGrantedAuthority(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + role.getName();
    }
}
