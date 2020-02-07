package org.art.demo.integration.config;

import org.art.demo.integration.transformer.SimpleFilePayloadTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.io.File;

@Profile("annotations")
@Configuration
@EnableIntegration
public class AnnotationBasedIntegrationConfig {

    private static final String INPUT_DIR = "src/main/resources/data/input";
    private static final String SUB_1_OUTPUT_DIR = "src/main/resources/data/output/subscriber1";
    private static final String SUB_2_OUTPUT_DIR = "src/main/resources/data/output/subscriber2";
    private static final String FILE_PATTERN = "*.txt";

    @Bean
    public MessageChannel pubSubFileChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    @BridgeFrom(value = "pubSubFileChannel")
    public MessageChannel fileChannel1() {
        return new DirectChannel();
    }

    @Bean
    @BridgeFrom(value = "pubSubFileChannel")
    public MessageChannel fileChannel2() {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "pubSubFileChannel", poller = @Poller(fixedDelay = "1000"))
    public MessageSource<File> fileReadingMessageSource() {
        FileReadingMessageSource sourceReader = new FileReadingMessageSource();
        sourceReader.setDirectory(new File(INPUT_DIR));
        sourceReader.setFilter(new SimplePatternFileListFilter(FILE_PATTERN));
        return sourceReader;
    }

    @Bean
    @Transformer(inputChannel = "fileChannel1", outputChannel = "transChannel1")
    public SimpleFilePayloadTransformer fileTransformer1() {
        return new SimpleFilePayloadTransformer("Transformer 1");
    }

    @Bean
    @Transformer(inputChannel = "fileChannel2", outputChannel = "transChannel2")
    public SimpleFilePayloadTransformer fileTransformer2() {
        return new SimpleFilePayloadTransformer("Transformer 2");
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
    @ServiceActivator(inputChannel = "transChannel1")
    public MessageHandler subscriber1FileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(SUB_1_OUTPUT_DIR));
        handler.setFileExistsMode(FileExistsMode.IGNORE);
        handler.setExpectReply(false);
        return handler;
    }

    @Bean
    @ServiceActivator(inputChannel = "transChannel2")
    public MessageHandler subscriber2FileWriter() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(SUB_2_OUTPUT_DIR));
        handler.setFileExistsMode(FileExistsMode.IGNORE);
        handler.setExpectReply(false);
        return handler;
    }
}
