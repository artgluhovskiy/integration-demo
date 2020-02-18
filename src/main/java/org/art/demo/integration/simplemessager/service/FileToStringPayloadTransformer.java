package org.art.demo.integration.simplemessager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
public class FileToStringPayloadTransformer extends AbstractPayloadTransformer<File, String> {

    private String name;

    public FileToStringPayloadTransformer(String name) {
        this.name = name;
    }

    @Override
    protected String transformPayload(File file) {
        log.info("File transformer: {}", name);
        StringBuilder sb = new StringBuilder(10);
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.warn("Exception occurred: {}", e.getMessage());
            sb.append("Error has occurred!!!\n");
        }
        sb.append('\n')
                .append("Modified by: ")
                .append(name).append('\n')
                .append("Date: ").append(LocalDateTime.now()).append('\n')
                .append("Have a nice day!");
        return sb.toString();
    }
}
