package com.bookstore.bookstore.controller.admin;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    // --- HIỂN THỊ DANH SÁCH SÁCH ---
    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            Model model) {

        int pageSize = 10;
        var pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());

        var booksPage = (categoryId != null)
                ? bookService.findByCategoryId(categoryId, pageable)
                : (StringUtils.hasText(search))
                ? bookService.searchBooks(search, pageable)
                : bookService.findAll(pageable);

        model.addAttribute("booksPage", booksPage);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("currentCategory", categoryId);
        model.addAttribute("searchKeyword", search);
        model.addAttribute("currentPage", page);

        return "admin/books/list";
    }

    // --- HIỂN THỊ FORM THÊM SÁCH MỚI ---
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("isEdit", false);
        return "admin/books/form";
    }

    // --- HIỂN THỊ FORM CHỈNH SỬA SÁCH (ĐÃ SỬA LỖI) ---
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        // Sửa lại theo cấu trúc service trả về Book, không phải Optional<Book>
        Book book = bookService.findById(id);

        if (book != null) { // Kiểm tra null thay vì isPresent()
            model.addAttribute("book", book);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("isEdit", true);
            return "admin/books/form";
        } else {
            ra.addFlashAttribute("error", "Không tìm thấy sách với ID: " + id);
            return "redirect:/admin/books";
        }
    }

    // --- LƯU SÁCH (THÊM MỚI HOẶC CẬP NHẬT) (ĐÃ SỬA LỖI) ---
    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") Book book,
                           @RequestParam("imageFile") MultipartFile imageFile,
                           RedirectAttributes ra) {
        try {
            // Gán Category cho sách
            if (book.getCategory() != null && book.getCategory().getId() != null) {
                var category = categoryService.findById(book.getCategory().getId());
                book.setCategory(category);
            }

            // Xử lý upload file ảnh mới
            if (imageFile != null && !imageFile.isEmpty()) {
                String uniqueFilename = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(imageFile.getOriginalFilename());
                Path uploadDir = Paths.get("book-uploads");

                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                try (InputStream inputStream = imageFile.getInputStream()) {
                    Path filePath = uploadDir.resolve(uniqueFilename);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                book.setImageUrl(uniqueFilename);
            } else {
                // Sửa lại logic giữ ảnh cũ khi chỉnh sửa
                if (book.getId() != null) {
                    Book existingBook = bookService.findById(book.getId());
                    if (existingBook != null) {
                        book.setImageUrl(existingBook.getImageUrl()); // Giữ lại ảnh cũ
                    }
                }
            }

            bookService.save(book);
            ra.addFlashAttribute("success", "Lưu sách thành công!");

        } catch (IOException e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Lỗi khi upload file ảnh: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Đã xảy ra lỗi khi lưu sách: " + e.getMessage());
        }

        return "redirect:/admin/books";
    }

    // --- XÓA SÁCH ---
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
