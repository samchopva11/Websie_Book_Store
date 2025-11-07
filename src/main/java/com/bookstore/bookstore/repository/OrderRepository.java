package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Tìm order theo order number
    Optional<Order> findByOrderNumber(String orderNumber);

    // Lấy orders của user
    Page<Order> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    // Lấy orders của user theo ID
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Lấy orders theo status
    Page<Order> findByStatus(String status, Pageable pageable);

    // Lấy orders mới nhất
    List<Order> findTop10ByOrderByCreatedAtDesc();

    // Đếm orders theo status
    long countByStatus(String status);

    // Tổng doanh thu
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED'")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    List<Order> findByUserId(@Param("userId") Long userId);

    // Doanh thu theo tháng
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED' " +
            "AND MONTH(o.createdAt) = :month AND YEAR(o.createdAt) = :year")
    BigDecimal calculateMonthlyRevenue(@Param("month") int month, @Param("year") int year);

    // Đếm orders trong khoảng thời gian
    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    long countOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate);

    // Tìm kiếm orders
    @Query("SELECT o FROM Order o WHERE o.orderNumber LIKE %:keyword% " +
            "OR o.receiverName LIKE %:keyword% OR o.shippingPhone LIKE %:keyword%")
    Page<Order> searchOrders(@Param("keyword") String keyword, Pageable pageable);
}
