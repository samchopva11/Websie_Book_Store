package com.bookstore.bookstore.controller.admin;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            Model model) {

        int pageSize = 10;
        var pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        var booksPage = (categoryId != null)
                ? bookService.findByCategoryId(categoryId, pageable)
                : (search != null && !search.isEmpty())
                ? bookService.searchBooks(search, pageable)
                : bookService.findAll(pageable);

        model.addAttribute("booksPage", booksPage);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("currentCategory", categoryId);
        model.addAttribute("searchKeyword", search);
        model.addAttribute("currentPage", page);

        return "admin/books/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("isEdit", false);
        return "admin/books/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("isEdit", true);
        return "admin/books/form";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute Book book, RedirectAttributes ra) {
        try {
            // Set category from categoryId
            if (book.getCategory() != null && book.getCategory().getId() != null) {
                var category = categoryService.findById(book.getCategory().getId());
                book.setCategory(category);
            }

            bookService.save(book);
            ra.addFlashAttribute("success", "Lưu sách thành công!");
            return "redirect:/admin/books";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
            return "redirect:/admin/books/new";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes ra) {
        try {
            bookService.deleteById(id);
            ra.addFlashAttribute("success", "Xóa sách thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa sách: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }
}
