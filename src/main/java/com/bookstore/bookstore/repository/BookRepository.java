package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Tìm sách theo category
    Page<Book> findByCategory(Category category, Pageable pageable);

    // Tìm sách theo category ID
    Page<Book> findByCategoryId(Long categoryId, Pageable pageable);

    // Tìm sách bestseller
    Page<Book> findByIsBestsellerTrue(Pageable pageable);

    // Tìm sách mới
    Page<Book> findByIsNewTrue(Pageable pageable);

    // Tìm kiếm sách theo title hoặc author
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);

    // Lấy sách mới nhất
    List<Book> findTop8ByOrderByCreatedAtDesc();

    // Lấy sách bestseller
    List<Book> findTop8ByIsBestsellerTrueOrderByRatingDesc();

    // Đếm số sách theo category
    long countByCategory(Category category);

    // Kiểm tra sách tồn tại theo title
    boolean existsByTitle(String title);
}