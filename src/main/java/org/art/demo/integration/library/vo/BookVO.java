package org.art.demo.integration.library.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.art.demo.integration.library.enumeration.Genre;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookVO {

    @Positive
    private long bookId;

    @NotBlank
    private String title;

    @NotNull
    private Genre genre;
}
