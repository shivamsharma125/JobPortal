package com.shivam.jobportal.controllers;

import com.shivam.jobportal.dtos.LoginRequestDto;
import com.shivam.jobportal.dtos.LoginResponseDto;
import com.shivam.jobportal.dtos.SignUpRequestDto;
import com.shivam.jobportal.exceptions.InvalidRequestException;
import com.shivam.jobportal.models.Role;
import com.shivam.jobportal.models.RoleDto;
import com.shivam.jobportal.models.User;
import com.shivam.jobportal.dtos.UserDto;
import com.shivam.jobportal.services.IAuthService;
import com.shivam.jobportal.utils.RequestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) {
        // validate input
        if (RequestUtils.isEmptyParam(request.getEmail())) throw new InvalidRequestException("Invalid email");
        if (RequestUtils.isEmptyParam(request.getPassword())) throw new InvalidRequestException("Invalid password");
        if (RequestUtils.isEmptyParam(request.getName())) throw new InvalidRequestException("Invalid name");
        if (RequestUtils.isEmptyParam(request.getRoleIds())) throw new InvalidRequestException("Invalid role");

        User user = authService.signUp(
                request.getName(), request.getEmail(),
                request.getPassword(), request.getRoleIds()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(from(user));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        // validate input
        if (RequestUtils.isEmptyParam(request.getEmail())) throw new InvalidRequestException("Invalid email");
        if (RequestUtils.isEmptyParam(request.getPassword())) throw new InvalidRequestException("Invalid password");

        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    private UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        if (user.getRoles() != null) {
            Set<RoleDto> roleDtos = new HashSet<>();
            for (Role role : user.getRoles()){
                RoleDto roleDto = new RoleDto();
                roleDto.setId(role.getId());
                roleDto.setName(role.getName());
                roleDtos.add(roleDto);
            }
            userDto.setRoles(roleDtos);
        }
        return userDto;
    }

}

