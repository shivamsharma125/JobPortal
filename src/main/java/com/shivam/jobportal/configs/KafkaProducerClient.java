package com.shivam.jobportal.configs;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducerClient {
    private final KafkaTemplate<String,String> kafkaTemplate;

    public KafkaProducerClient(KafkaTemplate<String,String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String event) {
        kafkaTemplate.send(topic, event);
    }
}
