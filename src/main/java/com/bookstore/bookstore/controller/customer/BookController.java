package com.bookstore.bookstore.controller.customer;

import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            Model model) {

        int pageSize = 12;
        var pageable = PageRequest.of(page, pageSize);
        var booksPage = (categoryId != null)
                ? bookService.findByCategoryId(categoryId, pageable)
                : (search != null && !search.isEmpty())
                ? bookService.searchBooks(search, pageable)
                : bookService.findAll(pageable);

        model.addAttribute("booksPage", booksPage);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("currentCategory", categoryId);
        model.addAttribute("searchKeyword", search);
        return "customer/books";
    }

    @GetMapping("/{id}")
    public String bookDetail(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "customer/book-detail";
    }
}
