package ru.pressstart9.petproject.person_ms.service.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.dto.BlankResponse;
import ru.pressstart9.petproject.dto.CreatedResponse;
import ru.pressstart9.petproject.dto.requests.CreatePersonBody;
import ru.pressstart9.petproject.dto.requests.RemovePetRequest;
import ru.pressstart9.petproject.person_ms.service.PersonService;

@Component
@KafkaListener(topics = "person-requests", groupId = "person-group")
public class ResponseProducer {
    private final KafkaTemplate<String, BlankResponse> kafkaTemplateBlank;
    private final KafkaTemplate<String, CreatedResponse> kafkaTemplateCreated;
    private final PersonService personService;

    public ResponseProducer(KafkaTemplate<String, BlankResponse> kafkaTemplateBlank, KafkaTemplate<String, CreatedResponse> kafkaTemplateCreated, PersonService personService) {
        this.kafkaTemplateBlank = kafkaTemplateBlank;
        this.kafkaTemplateCreated = kafkaTemplateCreated;
        this.personService = personService;
    }

    @KafkaHandler
    public void consumePersonResponse(@Header(KafkaHeaders.REPLY_TOPIC) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload CreatePersonBody data) {
        Long result = personService.createPerson(data.getName(), data.getBirthdate());

        ProducerRecord<String, CreatedResponse> reply = new ProducerRecord<>(replyTopic, new CreatedResponse(result));
        reply.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId));
        kafkaTemplateCreated.send(reply);
    }

    @KafkaHandler
    public void consumePersonResponse(@Header(KafkaHeaders.REPLY_TOPIC) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) String correlationId, @Payload RemovePetRequest data) {
        personService.removePetById(data.ownerId, data.petId);

        ProducerRecord<String, BlankResponse> reply = new ProducerRecord<>(replyTopic, new BlankResponse());
        reply.headers().add(new RecordHeader(KafkaHeaders.CORRELATION_ID, correlationId.getBytes()));
        kafkaTemplateBlank.send(reply);
    }
}
