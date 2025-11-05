package com.bookstore.bookstore.service.impl;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.Category;
import com.bookstore.bookstore.repository.BookRepository;
import com.bookstore.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sách với ID: " + id));
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(Long id) {
        Book book = findById(id);

        // Kiểm tra nếu sách đã có trong đơn hàng
        if (!book.getOrderItems().isEmpty()) {
            throw new RuntimeException("Không thể xóa sách đã có trong đơn hàng!");
        }

        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByCategory(Category category, Pageable pageable) {
        return bookRepository.findByCategory(category, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findBestsellers(Pageable pageable) {
        return bookRepository.findByIsBestsellerTrue(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> findNewBooks(Pageable pageable) {
        return bookRepository.findByIsNewTrue(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Book> searchBooks(String keyword, Pageable pageable) {
        return bookRepository.searchBooks(keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getLatestBooks() {
        return bookRepository.findTop8ByOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getBestsellerBooks() {
        return bookRepository.findTop8ByIsBestsellerTrueOrderByRatingDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return bookRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCategory(Category category) {
        return bookRepository.countByCategory(category);
    }
}
