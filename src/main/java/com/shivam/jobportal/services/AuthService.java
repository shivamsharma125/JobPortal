package com.shivam.jobportal.services;

import com.shivam.jobportal.exceptions.InvalidCredentialsException;
import com.shivam.jobportal.exceptions.InvalidRoleException;
import com.shivam.jobportal.exceptions.UserAlreadyExistsException;
import com.shivam.jobportal.models.State;
import com.shivam.jobportal.models.User;
import com.shivam.jobportal.models.Role;
import com.shivam.jobportal.repositories.RoleRepository;
import com.shivam.jobportal.repositories.UserRepository;
import com.shivam.jobportal.security.services.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService implements IAuthService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User signUp(String name, String email, String password, Set<Long> roleIds) {
        if (userRepository.existsByEmailAndState(email, State.ACTIVE)) {
            throw new UserAlreadyExistsException("Email is already registered.");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        Set<Role> roles = new HashSet<>();
        roleIds.forEach(roleId -> {
            Role userRole = roleRepository.findById(roleId)
                    .orElseThrow(() -> new InvalidRoleException("Invalid role - " + roleId));
            roles.add(userRole);
        });

        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return jwtService.generateToken(email);
    }
}
