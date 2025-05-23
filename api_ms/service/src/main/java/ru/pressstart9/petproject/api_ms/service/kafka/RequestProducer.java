package ru.pressstart9.petproject.api_ms.service.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.common_kafka.exceptions.ServiceNotResponding;
import ru.pressstart9.petproject.dto.requests.CreatePersonBody;
import ru.pressstart9.petproject.dto.*;
import ru.pressstart9.petproject.dto.requests.GetRequest;
import ru.pressstart9.petproject.dto.requests.RemovePetRequest;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class RequestProducer {
    private final ReplyingKafkaTemplate<String, CreatePersonBody, CreatedResponse> replyingKafkaTemplateCreatePerson;
    private final ReplyingKafkaTemplate<String, RemovePetRequest, BlankResponse> replyingKafkaTemplateRemovePet;
    private final ReplyingKafkaTemplate<String, GetRequest, PersonDto> replyingKafkaTemplateGetPerson;
    private final ReplyingKafkaTemplate<String, GetRequest, PetDto> replyingKafkaTemplateGetPet;

    @Value("${kafka.timeout.defaults}")
    private long defaultTimeoutS;

    public RequestProducer(ReplyingKafkaTemplate<String, CreatePersonBody, CreatedResponse> replyingKafkaTemplateCreatePerson, ReplyingKafkaTemplate<String, RemovePetRequest, BlankResponse> replyingKafkaTemplateRemovePet, ReplyingKafkaTemplate<String, GetRequest, PersonDto> replyingKafkaTemplateGetPerson, ReplyingKafkaTemplate<String, GetRequest, PetDto> replyingKafkaTemplateGetPet) {
        this.replyingKafkaTemplateCreatePerson = replyingKafkaTemplateCreatePerson;
        this.replyingKafkaTemplateRemovePet = replyingKafkaTemplateRemovePet;
        this.replyingKafkaTemplateGetPerson = replyingKafkaTemplateGetPerson;
        this.replyingKafkaTemplateGetPet = replyingKafkaTemplateGetPet;
    }

    public CreatedResponse sendPersonRequest(CreatePersonBody data) throws ExecutionException, InterruptedException {
        ProducerRecord<String, CreatePersonBody> record = new ProducerRecord<>("person-requests", data);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "api-responses".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8)));

        RequestReplyFuture<String, CreatePersonBody, CreatedResponse> reply = replyingKafkaTemplateCreatePerson.sendAndReceive(record, Duration.ofSeconds(defaultTimeoutS));
        return reply.get().value();
    }

    public PersonDto sendPersonRequest(GetRequest data) {
        ProducerRecord<String, GetRequest> record = new ProducerRecord<>("person-requests", data);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "api-responses".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        RequestReplyFuture<String, GetRequest, PersonDto> reply = replyingKafkaTemplateGetPerson.sendAndReceive(record, Duration.ofSeconds(defaultTimeoutS));
        try {
            return reply.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new ServiceNotResponding(e.getMessage());
        }
    }

    public PetDto sendPetRequest(GetRequest data) {
        ProducerRecord<String, GetRequest> record = new ProducerRecord<>("pet-requests", data);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "api-responses".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        RequestReplyFuture<String, GetRequest, PetDto> reply = replyingKafkaTemplateGetPet.sendAndReceive(record, Duration.ofSeconds(defaultTimeoutS));
        try {
            return reply.get().value();
        } catch (InterruptedException | ExecutionException e) {
            throw new ServiceNotResponding(e.getMessage());
        }
    }
}