package org.art.demo.integration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static org.art.demo.integration.config.kafka.KafkaConstants.CONSUMER_GROUP_ID;
import static org.art.demo.integration.config.kafka.KafkaConstants.DEMO_TOPIC_NAME;

@Slf4j
@Component
public class MessageKafkaListener {

    @KafkaListener(topics = DEMO_TOPIC_NAME, groupId = CONSUMER_GROUP_ID)
    public void listen(@Payload String message,
                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        log.info("Message received: {}, partition: {}", message, partition);
    }
}
