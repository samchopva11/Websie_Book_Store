package com.bookstore.bookstore.controller.customer;

import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("latestBooks", bookService.getLatestBooks());
        model.addAttribute("bestsellerBooks", bookService.getBestsellerBooks());
        model.addAttribute("categories", categoryService.findAll());
        return "customer/home";
    }
}
