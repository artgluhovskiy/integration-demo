package org.art.demo.integration.simplemessager.service;

import lombok.extern.slf4j.Slf4j;
import org.art.demo.integration.simplemessager.message.CustomMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static org.art.demo.integration.simplemessager.config.kafka.KafkaConstants.CONSUMER_GROUP_ID;
import static org.art.demo.integration.simplemessager.config.kafka.KafkaConstants.DEMO_TOPIC_NAME;

@Profile("messenger")
@Slf4j
@Component
public class MessageKafkaListener {

    @KafkaListener(topics = DEMO_TOPIC_NAME, groupId = CONSUMER_GROUP_ID)
    public void listen(@Payload CustomMessage message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("Message received: {}, partition: {}", message, partition);
    }
}
