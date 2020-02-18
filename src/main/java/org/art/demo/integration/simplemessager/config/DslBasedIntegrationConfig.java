package org.art.demo.integration.simplemessager.config;

import lombok.extern.slf4j.Slf4j;
import org.art.demo.integration.simplemessager.service.FileToStringPayloadTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.GenericSelector;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Slf4j
@Profile("dsl")
@Configuration
@EnableIntegration
public class DslBasedIntegrationConfig {

    private static final String INPUT_DIR = "src/main/resources/data/input";
    private static final String SUB_1_OUTPUT_DIR = "src/main/resources/data/output/subscriber1";
    private static final String SUB_2_OUTPUT_DIR = "src/main/resources/data/output/subscriber2";
    private static final String FILE_EXT = ".txt";
    private static final int FIXED_DELAY = 5000;

    @Bean
    public IntegrationFlow fileWritingDslFlow() {
        return IntegrationFlows.from(fileReadingMessageSource(),
                configured -> configured.poller(Pollers.fixedDelay(FIXED_DELAY)))
                .filter(txtFileFilter())
                .channel(pubSubFileChannel())
                .get();
    }

    @Bean
    public IntegrationFlow transformFlow1() {
        return f -> f.channel(pubSubFileChannel())
                .transform(fileTransformer1())
                .channel(transChannel1());
    }

    @Bean
    public IntegrationFlow transformFlow2() {
        return f -> f.channel(pubSubFileChannel())
                .transform(fileTransformer2())
                .channel(transChannel2());
    }

    @Bean
    public IntegrationFlow fileWriter1() {
        return f -> f.channel(transChannel1())
                .handle(subscriber1FileWriter());
    }

    @Bean
    public IntegrationFlow fileWriter2() {
        return f -> f.channel(transChannel2())
                .handle(subscriber2FileWriter());
    }

    @Bean
    public MessageChannel pubSubFileChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    public MessageChannel fileChannel1() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fileChannel2() {
        return new DirectChannel();
    }

    @Bean
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource sourceReader = new FileReadingMessageSource();
        sourceReader.setDirectory(new File(INPUT_DIR));
        return sourceReader;
    }

    @Bean
    public FileToStringPayloadTransformer fileTransformer1() {
        return new FileToStringPayloadTransformer("Transformer 1");
    }

    @Bean
    public FileToStringPayloadTransformer fileTransformer2() {
        return new FileToStringPayloadTransformer("Transformer 2");
    }

    @Bean
    public MessageChannel transChannel1() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel transChannel2() {
        return new DirectChannel();
    }

    @Bean
    public MessageHandler subscriber1FileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(SUB_1_OUTPUT_DIR));
        handler.setFileExistsMode(FileExistsMode.IGNORE);
        handler.setExpectReply(false);
        return handler;
    }

    @Bean
    public MessageHandler subscriber2FileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(SUB_2_OUTPUT_DIR));
        handler.setFileExistsMode(FileExistsMode.IGNORE);
        handler.setExpectReply(false);
        return handler;
    }

    @Bean
    public GenericSelector<File> txtFileFilter() {
        return source -> {
            log.info("Filtering ...");
            return source.getName().endsWith(FILE_EXT);
        };
    }
}
