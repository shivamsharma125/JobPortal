package com.shivam.jobportal.services;

import com.shivam.jobportal.exceptions.InvalidRoleException;
import com.shivam.jobportal.exceptions.UserAlreadyExistsException;
import com.shivam.jobportal.models.User;
import com.shivam.jobportal.models.Role;
import com.shivam.jobportal.repositories.RoleRepository;
import com.shivam.jobportal.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService implements IAuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User signUp(String name, String email, String password, Set<Long> roleIds) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email is already registered.");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        Set<Role> roles = new HashSet<>();
        roleIds.forEach(roleId -> {
            Role userRole = roleRepository.findById(roleId)
                    .orElseThrow(() -> new InvalidRoleException("Invalid role"));
            roles.add(userRole);
        });

        user.setRoles(roles);

        return userRepository.save(user);
    }
}
