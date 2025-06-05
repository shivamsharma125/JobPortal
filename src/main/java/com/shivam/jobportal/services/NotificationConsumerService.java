package com.shivam.jobportal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shivam.jobportal.dtos.EmailDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumerService {
    private final IEmailService emailService;
    private final ObjectMapper objectMapper;

    public NotificationConsumerService(IEmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "job-application-submitted", groupId = "email-group")
    public void consumeJobApplicationEvent(String message){
        EmailDto emailDto = getEmailDto(message);
        emailService.sendEmail(emailDto.getTo(),
                emailDto.getSubject(),
                emailDto.getBody());
    }

    @KafkaListener(topics = "job-application-status-updated", groupId = "email-group")
    public void consumeStatusChangeEvent(String message){
        EmailDto emailDto = getEmailDto(message);
        emailService.sendEmail(emailDto.getTo(),
                emailDto.getSubject(),
                emailDto.getBody());
    }

    private EmailDto getEmailDto(String message) {
        try {
            return objectMapper.readValue(message, EmailDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse email DTO from message: " + message, e);
        }
    }
}
