package com.harrishjoshi.springaop.audit.trails.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("""
            SELECT new com.harrishjoshi.springaop.audit.trails.book.BookResponse(
            b.id, b.title, b.description, b.createDate, b.lastModified, uc.firstName, um.firstName)
            FROM Book b
            JOIN User uc ON b.createdBy = uc.id
            LEFT JOIN User um ON b.lastModifiedBy = um.id
            WHERE b.isDeleted = false
            """)
    List<BookResponse> findAllBy();

    @Query("""
            SELECT new com.harrishjoshi.springaop.audit.trails.book.BookResponse(
            b.id, b.title, b.description, b.createDate, b.lastModified, uc.firstName, um.firstName)
            FROM Book b
            JOIN User uc ON b.createdBy = uc.id
            LEFT JOIN User um ON b.lastModifiedBy = um.id
            WHERE b.id = :id AND b.isDeleted = false
            """)
    Optional<BookResponse> findBookById(Integer id);
}
