package org.art.demo.integration.library.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.art.demo.integration.library.entity.Book;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final DirectChannel producerChannel;

    public Book processBook(Book book) {
        //Book processing ...
        producerChannel.send(new GenericMessage<>(book.toString(), buildHeaders(book)));
        return book;
    }

    private Map<String, Object> buildHeaders(Book book) {
        return Collections.singletonMap(KafkaHeaders.TOPIC, book.getGenre().name());
    }
}
