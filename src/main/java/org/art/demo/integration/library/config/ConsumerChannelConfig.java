package org.art.demo.integration.library.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.kafka.dsl.Kafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.messaging.PollableChannel;

import java.util.HashMap;
import java.util.Map;

import static org.art.demo.integration.library.CommonConstants.*;

@Profile("library")
@Slf4j
@Configuration
public class ConsumerChannelConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public IntegrationFlow kafkaConsumerFlow() {
        return IntegrationFlows
                .from(Kafka.messageDrivenChannelAdapter(kafkaListenerContainer()))
                .handle((payload, headers) -> {
                    log.info("New book consumed. Payload: {}, headers: {}", payload, headers);
                    //Some book processing ...
                    return null;
                })
                .get();
    }

    @Bean
    public PollableChannel consumerChannel() {
        return new QueueChannel();
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, String> kafkaListenerContainer() {
        ContainerProperties containerProps = new ContainerProperties(FANTASY_TOPIC_NAME, HORROR_TOPIC_NAME);
        return new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProps);
    }

    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, READERS_GROUP_1);
        return properties;
    }
}
