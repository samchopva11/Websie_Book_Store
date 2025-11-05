package com.bookstore.bookstore.service.impl;

import com.bookstore.bookstore.entity.Category;
import com.bookstore.bookstore.repository.CategoryRepository;
import com.bookstore.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + id));
    }

    @Override
    public Category save(Category category) {
        if (category.getId() == null && existsByName(category.getName())) {
            throw new RuntimeException("Danh mục đã tồn tại: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        Category category = findById(id);
        if (!category.getBooks().isEmpty()) {
            throw new RuntimeException("Không thể xóa danh mục có chứa sách!");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return categoryRepository.count();
    }
}
