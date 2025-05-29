package ru.pressstart9.petproject.pet_ms.service.kafka;

import lombok.SneakyThrows;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.commons.dto.requests.AddPetRequest;
import ru.pressstart9.petproject.commons.dto.requests.RemovePetOwnner;

import java.util.UUID;

@Component
public class RequestProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public RequestProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPersonRequest(RemovePetOwnner data) {
        send("person-requests", data);
    }

    public void sendPersonRequest(AddPetRequest data) {
        send("person-requests", data);
    }

    @SneakyThrows
    private void send(String topicName, Object data) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, data);
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        kafkaTemplate.send(record);
    }
}