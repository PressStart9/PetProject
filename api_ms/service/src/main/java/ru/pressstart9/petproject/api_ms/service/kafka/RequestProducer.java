package ru.pressstart9.petproject.api_ms.service.kafka;

import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.commons.dto.requests.*;
import ru.pressstart9.petproject.commons.exceptions.ServiceNotResponding;
import ru.pressstart9.petproject.commons.dto.responses.CreatedResponse;
import ru.pressstart9.petproject.commons.dto.responses.PersonDto;
import ru.pressstart9.petproject.commons.dto.responses.PetDto;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.lang.reflect.Constructor;

@Component
public class RequestProducer {
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Value("${kafka.timeout.defaults}")
    private long defaultTimeoutS;

    public RequestProducer(ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    public CreatedResponse sendPersonRequest(CreatePersonBody data) {
        return (CreatedResponse) sendAndReceive("person-requests", data);
    }

    public PersonDto sendPersonRequest(GetRequest data) {
        return (PersonDto) sendAndReceive("person-requests", data);
    }

    public void sendPersonRequest(DeleteRequest data) {
        sendAndReceive("person-requests", data);
    }

    public PetDto sendPetRequest(GetRequest data) {
        return (PetDto) sendAndReceive("pet-requests", data);
    }

    public List<PetDto> sendPetRequest(PetFilterRequest data) {
        return (List<PetDto>) sendAndReceive("pet-requests", data);
    }

    public CreatedResponse sendPetRequest(CreatePetBody data) {
        return (CreatedResponse) sendAndReceive("pet-requests", data);
    }

    public void sendPetRequest(DeleteRequest data) {
        sendAndReceive("pet-requests", data);
    }

    public void sendPetRequest(FriendPairBody data) {
        sendAndReceive("pet-requests", data);
    }

    public void sendPetRequest(RemoveFriendPair data) {
        sendAndReceive("pet-requests", data);
    }

    @SneakyThrows
    private Object sendAndReceive(String topicName, Object data) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, data);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "api-responses".getBytes()));
        record.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString().getBytes()));

        RequestReplyFuture<String, Object, Object> reply = replyingKafkaTemplate.sendAndReceive(record, Duration.ofSeconds(defaultTimeoutS));
        try {
            ConsumerRecord<String, Object> response = reply.get();
            Header exceptionHeader = response.headers().lastHeader(KafkaHeaders.EXCEPTION_FQCN);
            if (exceptionHeader != null) {
                Class<?> clazz = Class.forName(new String(exceptionHeader.value()));
                Constructor<?> constructor = clazz.getConstructor(String.class);

                Header exceptionMessage = response.headers().lastHeader(KafkaHeaders.EXCEPTION_MESSAGE);
                Object instance = constructor.newInstance(new String(exceptionMessage.value()));

                throw (Exception) instance;
            }

            return response.value();
        } catch (InterruptedException | ExecutionException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ServiceNotResponding(e.getMessage());
        }
    }
}