package com.harrishjoshi.springaop.audit.trails.book;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookResponse> getBooks() {
        return bookRepository.findAllBy();
    }

    public void addBook(BookRequest bookRequest) {
        var book = new Book();
        book.setTitle(bookRequest.title());
        book.setDescription(bookRequest.description());
        bookRepository.save(book);
    }

    public BookResponse getBook(Integer id) {
        return bookRepository.findBookById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: " + id + " not found."));
    }

    public void updateBook(Integer id, BookRequest bookRequest) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: " + id + " not found."));

        book.setTitle(bookRequest.title());
        book.setDescription(bookRequest.description());
        bookRepository.save(book);
    }

    public void deleteBook(Integer id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: " + id + " not found."));

        book.setDeleted(true);
        bookRepository.save(book);
    }
}
