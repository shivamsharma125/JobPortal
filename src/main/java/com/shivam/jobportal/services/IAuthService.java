package com.shivam.jobportal.services;

import com.shivam.jobportal.models.User;

import java.util.Set;

public interface IAuthService {
    User signUp(String name, String email, String password, Set<Long> roleIds);
}
