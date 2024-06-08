package com.harrishjoshi.springaop.audit.trails.book;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks() {
        List<BookResponse> books = bookService.getBooks();
        return ResponseEntity.ok().body(books);
    }

    @PostMapping
    public ResponseEntity<Void> addBook(@Valid @RequestBody BookRequest bookRequest) {
        bookService.addBook(bookRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Integer id) {
        BookResponse book = bookService.getBook(id);
        return ResponseEntity.ok().body(book);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateBook(@PathVariable Integer id, @Valid @RequestBody BookRequest bookRequest) {
        bookService.updateBook(id, bookRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}