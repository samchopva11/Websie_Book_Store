package com.bookstore.bookstore.service;

import com.bookstore.bookstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserService {
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    User findById(Long id);
    User findByUsername(String username);
    User save(User user);
    void deleteById(Long id);

    // Authentication
    User register(User user);
    User login(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Customer management
    Page<User> findCustomers(Pageable pageable);
    Page<User> searchCustomers(String keyword, Pageable pageable);
    long countCustomers();
}