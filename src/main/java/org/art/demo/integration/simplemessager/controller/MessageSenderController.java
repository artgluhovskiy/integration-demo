package org.art.demo.integration.simplemessager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.art.demo.integration.simplemessager.message.CustomMessage;
import org.art.demo.integration.simplemessager.service.KafkaMessageSender;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("messenger")
@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageSenderController {

    private final KafkaMessageSender messageSender;

    @GetMapping("/send/{msg}/{name}")
    public String sendMessage(@PathVariable("msg") String msg, @PathVariable("name") String name) {
        log.info("Sending message: {} + {}", msg, name);
        CustomMessage message = new CustomMessage(msg, name);
        messageSender.sendMessage(message);
        return "Message Sent: " + message.toString();
    }
}
