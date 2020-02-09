package org.art.demo.integration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static org.art.demo.integration.config.kafka.KafkaConstants.DEMO_TOPIC_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageSender {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String msg) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(DEMO_TOPIC_NAME, msg);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
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
