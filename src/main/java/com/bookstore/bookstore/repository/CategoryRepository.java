package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Tìm category theo tên
    Optional<Category> findByName(String name);

    // Kiểm tra category tồn tại theo tên
    boolean existsByName(String name);

    // Đếm số sách trong category
    // Sẽ sử dụng query trong service layer
}
