package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.CartItem;
import com.bookstore.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Lấy tất cả items trong cart của user
    List<CartItem> findByUser(User user);

    // Lấy cart items theo user ID
    List<CartItem> findByUserId(Long userId);

    // Tìm item cụ thể trong cart
    Optional<CartItem> findByUserAndBook(User user, Book book);

    // Xóa tất cả items của user
    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.user.id = :userId")
    void deleteByUserId(Long userId);

    // Đếm số items trong cart
    long countByUser(User user);

    // Kiểm tra sách đã có trong cart chưa
    boolean existsByUserAndBook(User user, Book book);
}