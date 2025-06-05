package com.shivam.jobportal.configs;

import com.shivam.jobportal.models.Role;
import com.shivam.jobportal.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleSeeder {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void injectRoles() {
        Set<String> defaultRoles = Set.of("APPLICANT", "RECRUITER", "ADMIN");

        List<Role> existingRoles = roleRepository.findAll();
        Set<String> existingRoleNames = existingRoles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        defaultRoles.stream()
                .filter(roleName -> !existingRoleNames.contains(roleName))
                .forEach(roleName -> {
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    newRole.setDescription(roleName + " role");
                    roleRepository.save(newRole);
                });
    }
}

