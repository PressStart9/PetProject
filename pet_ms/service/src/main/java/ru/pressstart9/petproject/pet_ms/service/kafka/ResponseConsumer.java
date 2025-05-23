package ru.pressstart9.petproject.pet_ms.service.kafka;

import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.dto.BlankResponse;
import ru.pressstart9.petproject.dto.ResponseKey;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@KafkaListener(topics = "person-responses", groupId = "pet-group")
public class ResponseConsumer {
    private static final Map<UUID, CompletableFuture<BlankResponse>> pendingBlankRequests = new ConcurrentHashMap<>();

    public static void addPendingRequest(UUID uuid, CompletableFuture<BlankResponse> future) {
        pendingBlankRequests.put(uuid, future);
    }

    @KafkaHandler
    public void consumeBlankResponse(@Header(KafkaHeaders.RECEIVED_KEY) ResponseKey key, @Payload BlankResponse message) {
        CompletableFuture<BlankResponse> future = pendingBlankRequests.remove(key.uuid);
        if (future != null) {
            future.complete(message);
        }
    }
}
