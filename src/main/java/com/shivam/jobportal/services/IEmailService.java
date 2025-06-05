package com.shivam.jobportal.services;

public interface IEmailService {
    void sendEmail(String toEmail, String subject, String body);
}
