package ru.pressstart9.petproject.pet_ms.service.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.dto.BlankResponse;
import ru.pressstart9.petproject.dto.requests.RemovePetRequest;
import ru.pressstart9.petproject.dto.ResponseKey;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class RequestProducer {
    private final KafkaTemplate<Object, Object> kafkaTemplate;

    @Value("${kafka.timeout.defaults}")
    private long defaultTimeoutS;

    public RequestProducer(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<BlankResponse> sendPersonRequest(RemovePetRequest data) {
        CompletableFuture<BlankResponse> future = new CompletableFuture<>();
        ResponseKey key = new ResponseKey(UUID.randomUUID(), "pet-responses");
        ResponseConsumer.addPendingRequest(key.uuid, future);

        kafkaTemplate.send("person-requests", key, data);
        return future.orTimeout(defaultTimeoutS, TimeUnit.SECONDS);
    }
}