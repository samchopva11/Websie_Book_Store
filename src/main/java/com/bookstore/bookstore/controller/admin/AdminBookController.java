package com.bookstore.bookstore.controller.admin;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public String listBooks(@RequestParam(defaultValue = "0") int page, Model model) {
        var booksPage = bookService.findAll(PageRequest.of(page, 10));
        model.addAttribute("booksPage", booksPage);
        return "admin/books/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/books/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/books/form";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute Book book, RedirectAttributes ra) {
        try {
            bookService.save(book);
            ra.addFlashAttribute("success", "Lưu sách thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes ra) {
        try {
            bookService.deleteById(id);
            ra.addFlashAttribute("success", "Xóa sách thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/books";
    }
}
