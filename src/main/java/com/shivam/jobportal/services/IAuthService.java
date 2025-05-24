package com.shivam.jobportal.services;

import com.shivam.jobportal.models.User;

public interface IAuthService {
    User signUp(String name, String email, String password, Long roleId);
}
