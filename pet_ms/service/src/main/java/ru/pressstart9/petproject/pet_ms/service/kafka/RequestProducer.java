package ru.pressstart9.petproject.pet_ms.service.kafka;

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
import ru.pressstart9.petproject.commons.exceptions.ServiceNotResponding;
import ru.pressstart9.petproject.commons.dto.requests.RemovePetRequest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
public class RequestProducer {
    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @Value("${kafka.timeout.defaults}")
    private long defaultTimeoutS;

    public RequestProducer(ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate) {
        this.replyingKafkaTemplate = replyingKafkaTemplate;
    }

    public void sendPersonRequest(RemovePetRequest data) {
        sendAndReceive("person-requests", data);
    }

    @SneakyThrows
    private Object sendAndReceive(String topicName, Object data) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, data);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, "pet-responses".getBytes()));
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
        } catch (InterruptedException | ExecutionException | ClassNotFoundException | NoSuchMethodException |
                 InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ServiceNotResponding(e.getMessage());
        }
    }
}