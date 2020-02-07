package org.art.demo.integration.transformer;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class SimpleFilePayloadTransformer extends AbstractPayloadTransformer<File, String> {

    private String name;

    public SimpleFilePayloadTransformer(String name) {
        this.name = name;
    }

    @Override
    protected String transformPayload(File file) {
        System.out.println("File transformer: " + name);
        StringBuilder sb = new StringBuilder(10);
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = input.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
