package com.shivam.jobportal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shivam.jobportal.configs.KafkaProducerClient;
import com.shivam.jobportal.dtos.ApplicationStatusChangeEvent;
import com.shivam.jobportal.dtos.EmailDto;
import com.shivam.jobportal.dtos.JobApplicationEvent;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducerService {
    private final KafkaProducerClient kafkaProducerClient;
    private final ObjectMapper objectMapper;

    public NotificationProducerService(KafkaProducerClient kafkaProducerClient, ObjectMapper objectMapper) {
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
    }

    public void sendJobApplicationEvent(JobApplicationEvent event) {
        String message = "You have successfully applied for the job: " + event.getJobTitle()
                + " on " + event.getAppliedAt();

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(event.getApplicantEmail());
        emailDto.setSubject("Job Application Submitted");
        emailDto.setBody(message);
        sendEmailNotification("job-application-submitted", emailDto);
    }

    public void sendStatusChangeEvent(ApplicationStatusChangeEvent event) {
        String message = "Your application for job: " + event.getJobTitle()
                + " has been updated to: " + event.getNewStatus()
                + " on " + event.getUpdatedAt();

        EmailDto emailDto = new EmailDto();
        emailDto.setTo(event.getApplicantEmail());
        emailDto.setSubject("Application Status Updated");
        emailDto.setBody(message);
        sendEmailNotification("job-application-status-updated", emailDto);
    }

    private void sendEmailNotification(String topic, EmailDto emailDto) {
        try {
            kafkaProducerClient.sendMessage(topic, objectMapper.writeValueAsString(emailDto));
        } catch (JsonProcessingException e) {
            System.out.println("Something went wrong while sending a message to Kafka");
        }
    }
}
