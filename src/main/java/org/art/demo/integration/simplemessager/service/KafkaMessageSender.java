package org.art.demo.integration.simplemessager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.art.demo.integration.simplemessager.message.CustomMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static org.art.demo.integration.simplemessager.config.kafka.KafkaConstants.DEMO_TOPIC_NAME;

@Profile("messenger")
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageSender {

    private final KafkaTemplate<String, CustomMessage> kafkaTemplate;

    public void sendMessage(CustomMessage msg) {
        ListenableFuture<SendResult<String, CustomMessage>> future = kafkaTemplate.send(DEMO_TOPIC_NAME, msg);

        future.addCallback(new ListenableFutureCallback<SendResult<String, CustomMessage>>() {

            @Override
            public void onSuccess(SendResult<String, CustomMessage> result) {
                log.info("Sent message=[{}] with offset=[{}}], topic=[{}]",
                        msg, result.getRecordMetadata().offset(), result.getRecordMetadata().topic());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[{}] due to : {}", msg, ex.getMessage());
            }
        });
    }
}
