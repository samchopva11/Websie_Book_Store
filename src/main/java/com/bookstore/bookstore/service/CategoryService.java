package com.bookstore.bookstore.service;

import com.bookstore.bookstore.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Page<Category> findAll(Pageable pageable);
    Category findById(Long id);
    Category save(Category category);
    void deleteById(Long id);
    boolean existsByName(String name);
    long count();
}
