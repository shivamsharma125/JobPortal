package com.shivam.jobportal.controllers;

import com.shivam.jobportal.dtos.SignUpRequest;
import com.shivam.jobportal.models.User;
import com.shivam.jobportal.models.UserDto;
import com.shivam.jobportal.services.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpRequest request) {
        User user = authService.signUp(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRoleId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(from(user));
    }

    private UserDto from(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }

}

