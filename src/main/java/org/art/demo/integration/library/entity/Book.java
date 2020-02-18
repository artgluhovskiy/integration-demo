package org.art.demo.integration.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.art.demo.integration.library.enumeration.Genre;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    private long bookId;

    private String title;

    private Genre genre;
}
