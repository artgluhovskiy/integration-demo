package org.art.demo.integration.library.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static org.art.demo.integration.library.CommonConstants.*;

@Profile("library")
@Slf4j
@Component
public class BookKafkaListener {

    @KafkaListener(topics = FANTASY_TOPIC_NAME, groupId = READERS_GROUP_2)
    public void fantasyBookListener(@Payload String book,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Fantasy book message received: {}, topic: {}, partition: {}", book, topic, partition);
    }

    @KafkaListener(topics = HORROR_TOPIC_NAME, groupId = READERS_GROUP_2)
    public void horrorBookListener(@Payload String book,
                                   @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                   @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Horror book message received: {}, topic: {}, partition: {}", book, topic, partition);
    }
}


