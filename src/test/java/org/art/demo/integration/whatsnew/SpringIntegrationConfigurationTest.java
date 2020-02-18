package org.art.demo.integration.whatsnew;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.art.demo.integration.whatsnew.SpringIntegrationConfiguration.REACTIVE_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.web-application-type=reactive"
)
class SpringIntegrationConfigurationTest {

    @Autowired
    private SubscribableChannel input;

    @Autowired
    private PollableChannel output;

    @LocalServerPort
    private int port;

    @Test
    void splitAggregateFlowTest() {
        input.send(new GenericMessage<>("a,b,c,d"));
        Message<?> message = output.receive();

        assertNotNull(message);
        assertEquals("[A, B, C, D]", message.getPayload().toString());
    }

    @Test
    void sseFlowTest() {
        WebTestClient webTestClient =
                WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();

        Flux<String> responseBody = webTestClient
                .get()
                .uri(REACTIVE_ENDPOINT)
                .exchange()
                .returnResult(String.class)
                .getResponseBody();

        responseBody = responseBody.log();

        StepVerifier
                .create(responseBody)
                .expectNext("foo", "bar", "baz")
                .verifyComplete();
    }
}