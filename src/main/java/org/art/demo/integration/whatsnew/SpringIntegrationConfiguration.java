package org.art.demo.integration.whatsnew;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.SubscribableChannel;
import reactor.core.publisher.Flux;

import java.util.concurrent.Executors;

@Configuration
@EnableIntegration
public class SpringIntegrationConfiguration {

    static final String DELIMITER = ",";
    static final String REACTIVE_ENDPOINT = "/sse";

    @Bean
    public SubscribableChannel input() {
        return new DirectChannel();
    }

    @Bean
    public PollableChannel output() {
        return new QueueChannel();
    }

    @Bean
    public IntegrationFlow splitAggregateFlow() {
        return IntegrationFlows
                .from(input())
                .split(splitter -> splitter.delimiters(DELIMITER))
                .channel(c -> c.executor(Executors.newCachedThreadPool()))
                .<String, String>transform(String::toUpperCase)
                .resequence()
                .log(LoggingHandler.Level.WARN)
                .aggregate()
                .channel(output())
                .get();
    }

    @Bean
    public IntegrationFlow sseFlow() {
        return IntegrationFlows
                .from(WebFlux.inboundGateway(REACTIVE_ENDPOINT)
                .requestMapping(m -> m.produces(MediaType.TEXT_EVENT_STREAM_VALUE)))
                .handle((p, h) -> Flux.just("foo", "bar", "baz"))
                .get();
    }
}
