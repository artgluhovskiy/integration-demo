package org.art.demo.integration.simplemessager.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomMessage {

    private String msg;
    private String name;
}
