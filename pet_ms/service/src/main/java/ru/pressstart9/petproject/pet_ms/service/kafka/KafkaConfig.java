package ru.pressstart9.petproject.pet_ms.service.kafka;

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
    public ConcurrentMessageListenerContainer<String, Object> replyContainer(
            ConsumerFactory<String, Object> cf) {
        ContainerProperties containerProperties = new ContainerProperties("pet-responses");
        containerProperties.setGroupId("pet-group");
        return new ConcurrentMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate(
            ProducerFactory<String, Object> pf,
            ConcurrentMessageListenerContainer<String, Object> container) {
        return new ReplyingKafkaTemplate<>(pf, container);
    }

    @Bean
    public NewTopic petRequestsTopic() {
        return TopicBuilder.name("pet-requests")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic petResponsesTopic() {
        return TopicBuilder.name("pet-responses")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
