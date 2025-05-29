package ru.pressstart9.petproject.pet_ms.service.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.pressstart9.petproject.commons.AvailableColor;
import ru.pressstart9.petproject.commons.dto.requests.*;
import ru.pressstart9.petproject.commons.dto.responses.BlankResponse;
import ru.pressstart9.petproject.commons.dto.responses.CreatedResponse;
import ru.pressstart9.petproject.commons.dto.responses.PetDto;
import ru.pressstart9.petproject.pet_ms.service.PetService;

import java.util.List;

@Component
@KafkaListener(topics = "pet-requests", groupId = "pet-group")
public class ResponseProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final PetService petService;

    public ResponseProducer(KafkaTemplate<String, Object> kafkaTemplate, PetService petService) {
        this.kafkaTemplate = kafkaTemplate;
        this.petService = petService;
    }

    @KafkaHandler
    public void consumePersonResponse(@Header(value = KafkaHeaders.REPLY_TOPIC, required = false) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload AddPetRequest data) {
        try {
            petService.addPetById(data.ownerId, data.petId);

            reply(replyTopic, correlationId, new BlankResponse());
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new BlankResponse());
        }
    }

    @KafkaHandler
    public void consumePetResponse(@Header(KafkaHeaders.REPLY_TOPIC) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload CreatePetBody data) {
        try {
            Long result = petService.createPet(data.getName(), data.getBirthdate(), data.getBreed(), AvailableColor.valueOf(data.getColor()), data.getOwnerId());

            reply(replyTopic, correlationId, new CreatedResponse(result));
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new CreatedResponse());
        }
    }

    @KafkaHandler
    public void consumePetResponse(@Header(KafkaHeaders.REPLY_TOPIC) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload GetRequest data) {
        try {
            PetDto result = petService.getPetDtoById(data.getId());

            reply(replyTopic, correlationId, result);
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new BlankResponse());
        }
    }

    @KafkaHandler
    public void consumePetResponse(@Header(value = KafkaHeaders.REPLY_TOPIC, required = false) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload DeleteRequest data) {
        try {
            petService.deletePetById(data.getId());

            reply(replyTopic, correlationId, new BlankResponse());
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new BlankResponse());
        }
    }

    @KafkaHandler
    public void consumePetResponse(@Header(value = KafkaHeaders.REPLY_TOPIC, required = false) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload FriendPairBody data) {
        try {
            petService.addFriend(data.getPetId(), data.getFriendId());

            reply(replyTopic, correlationId, new BlankResponse());
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new BlankResponse());
        }
    }

    @KafkaHandler
    public void consumePetResponse(@Header(value = KafkaHeaders.REPLY_TOPIC, required = false) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload RemoveFriendPair data) {
        try {
            petService.removeFriend(data.getPetId(), data.getFriendId());

            reply(replyTopic, correlationId, new BlankResponse());
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new BlankResponse());
        }
    }

    @KafkaHandler
    public void consumePetResponse(@Header(KafkaHeaders.REPLY_TOPIC) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload GetByOwner data) {
        try {
            List<Long> result = petService.getByOwner(data.getOwnerId());

            reply(replyTopic, correlationId, result);
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, List.of());
        }
    }

    @KafkaHandler
    public void consumePetResponse(@Header(value = KafkaHeaders.REPLY_TOPIC, required = false) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload RemovePetsOwner data) {
        try {
            petService.removePetOwner(data.getOwnerId());

            reply(replyTopic, correlationId, new BlankResponse());
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, new BlankResponse());
        }
    }

    @KafkaHandler
    public void consumePetResponse(@Header(KafkaHeaders.REPLY_TOPIC) String replyTopic, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlationId, @Payload PetFilterRequest data) {
        try {
            List<AvailableColor> parsedColors = data.getColors().stream()
                    .filter(c -> !c.isBlank())
                    .map(String::toUpperCase)
                    .map(AvailableColor::valueOf)
                    .toList();

            List<PetDto> result = petService.getByParams(data.getName(), data.getBreed(), parsedColors, data.getSize(), data.getPage());

            reply(replyTopic, correlationId, result);
        } catch (Exception e) {
            exceptionallyReply(replyTopic, correlationId, e, List.of());
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
