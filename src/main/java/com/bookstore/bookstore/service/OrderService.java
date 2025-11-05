package com.bookstore.bookstore.service;

import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Page<Order> findAll(Pageable pageable);
    Order findById(Long id);
    Order findByOrderNumber(String orderNumber);
    Order save(Order order);
    void deleteById(Long id);

    // Order management
    Order createOrder(Order order, User user);
    Order updateOrderStatus(Long orderId, String status);
    List<Order> findOrdersByUser(Long userId);
    Page<Order> findByStatus(String status, Pageable pageable);
    Page<Order> searchOrders(String keyword, Pageable pageable);

    // Statistics
    long countByStatus(String status);
    BigDecimal calculateTotalRevenue();
    BigDecimal calculateMonthlyRevenue(int month, int year);
    List<Order> getRecentOrders();
}
