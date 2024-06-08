package com.harrishjoshi.springaop.audit.trails.book;

import com.harrishjoshi.springaop.audit.trails.audit.ActionCode;
import com.harrishjoshi.springaop.audit.trails.audit.EventLog;
import com.harrishjoshi.springaop.audit.trails.helper.AppContext;
import com.harrishjoshi.springaop.audit.trails.helper.ContextKey;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

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

    @EventLog(entityName = "Book", functionCode = "ADD_BOOK", actionCode = ActionCode.ADD)
    public void addBook(BookRequest bookRequest) {
        var book = new Book();
        book.setTitle(bookRequest.title());
        book.setDescription(bookRequest.description());
        bookRepository.save(book);
    }

    @EventLog(entityName = "Book", functionCode = "VIEW_BOOK")
    public BookResponse getBook(Integer id) {
        return bookRepository.findBookById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: " + id + " not found."));
    }

    @EventLog(entityName = "Book", functionCode = "UPDATE_BOOK", actionCode = ActionCode.UPDATE)
    public void updateBook(Integer id, BookRequest bookRequest) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: " + id + " not found."));
        AppContext.setDetails(ContextKey.PRE, SerializationUtils.clone(book));

        book.setTitle(bookRequest.title());
        book.setDescription(bookRequest.description());
        var updatedBook = bookRepository.save(book);
        AppContext.setDetails(ContextKey.POST, SerializationUtils.clone(updatedBook));
    }

    @EventLog(entityName = "Book", functionCode = "DELETE_BOOK", actionCode = ActionCode.DELETE)
    public void deleteBook(Integer id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id: " + id + " not found."));

        book.setDeleted(true);
        bookRepository.save(book);
    }
}
