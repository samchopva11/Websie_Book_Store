package com.bookstore.bookstore.service;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookService {
    List<Book> findAll();
    Page<Book> findAll(Pageable pageable);
    Book findById(Long id);
    Book save(Book book);
    void deleteById(Long id);

    // Advanced queries
    Page<Book> findByCategory(Category category, Pageable pageable);
    Page<Book> findByCategoryId(Long categoryId, Pageable pageable);
    Page<Book> findBestsellers(Pageable pageable);
    Page<Book> findNewBooks(Pageable pageable);
    Page<Book> searchBooks(String keyword, Pageable pageable);

    // For homepage
    List<Book> getLatestBooks();
    List<Book> getBestsellerBooks();

    // Statistics
    long count();
    long countByCategory(Category category);
}
