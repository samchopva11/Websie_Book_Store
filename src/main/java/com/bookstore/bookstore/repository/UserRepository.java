package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo username
    Optional<User> findByUsername(String username);

    // Tìm user theo email
    Optional<User> findByEmail(String email);

    // Kiểm tra username tồn tại
    boolean existsByUsername(String username);

    // Kiểm tra email tồn tại
    boolean existsByEmail(String email);

    // Lấy tất cả customers (không phải admin)
    Page<User> findByRole(String role, Pageable pageable);

    // Lấy customers theo trạng thái active
    List<User> findByRoleAndIsActive(String role, Boolean isActive);

    // Đếm số customers
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = 'CUSTOMER'")
    long countCustomers();

    // Tìm kiếm user theo nhiều tiêu chí
    @Query("SELECT u FROM User u WHERE u.role = 'CUSTOMER' AND " +
            "(LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> searchCustomers(String keyword, Pageable pageable);
}
