package com.bookstore.bookstore.repository;

import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Lấy items của một order
    List<OrderItem> findByOrder(Order order);

    // Lấy items theo order ID
    List<OrderItem> findByOrderId(Long orderId);

    // Thống kê sách bán chạy nhất
    @Query("SELECT oi.book.id, oi.book.title, SUM(oi.quantity) as totalSold " +
            "FROM OrderItem oi GROUP BY oi.book.id, oi.book.title " +
            "ORDER BY totalSold DESC")
    List<Object[]> findBestSellingBooks();
}
