package ru.pressstart9.petproject.api_ms.service.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import ru.pressstart9.petproject.dto.BlankResponse;
import ru.pressstart9.petproject.dto.CreatedResponse;
import ru.pressstart9.petproject.dto.PersonDto;
import ru.pressstart9.petproject.dto.PetDto;
import ru.pressstart9.petproject.dto.requests.CreatePersonBody;
import ru.pressstart9.petproject.dto.requests.GetRequest;
import ru.pressstart9.petproject.dto.requests.RemovePetRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, CreatedResponse> replyContainerCreated(
            ConsumerFactory<String, CreatedResponse> cf) {
        ContainerProperties containerProperties = new ContainerProperties("api-responses");
        containerProperties.setGroupId("api-group");
        return new ConcurrentMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, BlankResponse> replyContainerBlank(
            ConsumerFactory<String, BlankResponse> cf) {
        ContainerProperties containerProperties = new ContainerProperties("api-responses");
        containerProperties.setGroupId("api-group");
        return new ConcurrentMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, PersonDto> replyContainerPersonDto(
            ConsumerFactory<String, PersonDto> cf) {
        ContainerProperties containerProperties = new ContainerProperties("api-responses");
        containerProperties.setGroupId("api-group");
        return new ConcurrentMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, PetDto> replyContainerPetDto(
            ConsumerFactory<String, Object> cf) {
        ContainerProperties containerProperties = new ContainerProperties("api-responses");
        containerProperties.setGroupId("api-group");
        return new ConcurrentMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, CreatePersonBody, CreatedResponse> replyingKafkaTemplateCreatePerson(
            ProducerFactory<String, CreatePersonBody> pf,
            ConcurrentMessageListenerContainer<String, CreatedResponse> container) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public ReplyingKafkaTemplate<String, RemovePetRequest, BlankResponse> replyingKafkaTemplateRemovePet(
            ProducerFactory<String, RemovePetRequest> pf,
            ConcurrentMessageListenerContainer<String, BlankResponse> container) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public ReplyingKafkaTemplate<String, GetRequest, PersonDto> replyingKafkaTemplateGetPerson(
            ProducerFactory<String, GetRequest> pf,
            ConcurrentMessageListenerContainer<String, PersonDto> container) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public ReplyingKafkaTemplate<String, GetRequest, PetDto> replyingKafkaTemplateGetPet(
            ProducerFactory<String, GetRequest> pf,
            ConcurrentMessageListenerContainer<String, PetDto> container) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public NewTopic apiResponsesTopic() {
        return TopicBuilder.name("api-responses")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
