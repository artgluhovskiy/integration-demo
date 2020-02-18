package org.art.demo.integration.library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.art.demo.integration.library.entity.Book;
import org.art.demo.integration.library.service.BookService;
import org.art.demo.integration.library.vo.BookVO;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final ModelMapper mapper = new ModelMapper();

    @PostMapping
    public ResponseEntity<BookVO> publishBook(@Valid @RequestBody BookVO bookVO) {
        log.info("Publishing new book: {}", bookVO);
        Book book = mapper.map(bookVO, Book.class);
        bookService.processBook(book);
        return ResponseEntity.ok(bookVO);
    }
}
