package ru.pressstart9.petproject.person_ms.service.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.commons.dto.requests.*;
import ru.pressstart9.petproject.commons.dto.responses.BlankResponse;
import ru.pressstart9.petproject.commons.dto.responses.CreatedResponse;
import ru.pressstart9.petproject.commons.dto.responses.PersonDto;
import ru.pressstart9.petproject.person_ms.service.PersonService;

@Component
@KafkaListener(topics = "person-requests", groupId = "person-group")
public class ResponseProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PersonService personService;

    public ResponseProducer(KafkaTemplate<String, Object> kafkaTemplate, PersonService personService) {
        this.kafkaTemplate = kafkaTemplate;
        this.personService = personService;
    }

    @KafkaHandler
    public void consumePersonResponse(@Header(KafkaHeaders.REPLY_TOPIC) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload CreatePersonBody data) {
        try {
            Long result = personService.createPerson(data.getName(), data.getBirthdate());

            reply(replyTopic, correlationId, new CreatedResponse(result));
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new CreatedResponse());
        }
    }

    @KafkaHandler
    public void consumePersonResponse(@Header(KafkaHeaders.REPLY_TOPIC) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload GetRequest data) {
        try {
            PersonDto result = personService.getPersonDtoById(data.getId());

            reply(replyTopic, correlationId, result);
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new PersonDto());
        }
    }

    @KafkaHandler
    public void consumePersonResponse(@Header(value = KafkaHeaders.REPLY_TOPIC, required = false) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload DeleteRequest data) {
        try {
            personService.deletePersonById(data.getId());

            reply(replyTopic, correlationId, new BlankResponse());
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new BlankResponse());
        }
    }

    private void reply(String replyTopicName, byte[] correlationId, Object data) {
        if (replyTopicName == null || replyTopicName.isEmpty()) {
            return;
        }
        ProducerRecord<String, Object> reply = new ProducerRecord<>(replyTopicName, data);
        reply.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId));
        kafkaTemplate.send(reply);
    }

    private void exceptionallyReply(String replyTopicName, byte[] correlationId, Exception e, Object data) {
        ProducerRecord<String, Object> reply = new ProducerRecord<>(replyTopicName, data);
        reply.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId));
        reply.headers().add(new RecordHeader(KafkaHeaders.EXCEPTION_FQCN, e.getClass().getTypeName().getBytes()));
        reply.headers().add(new RecordHeader(KafkaHeaders.EXCEPTION_MESSAGE, e.getMessage().getBytes()));
        kafkaTemplate.send(reply);
    }
}
