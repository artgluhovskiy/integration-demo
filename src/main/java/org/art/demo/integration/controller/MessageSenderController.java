package org.art.demo.integration.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.art.demo.integration.service.KafkaMessageSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageSenderController {

    private final KafkaMessageSender messageSender;

    @GetMapping("/send/{msg}")
    public void sendMessage(@PathVariable("msg") String msg) {
        log.info("Sending message: {}", msg);
        messageSender.sendMessage(msg);
    }
}
