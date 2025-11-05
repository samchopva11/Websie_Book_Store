package com.bookstore.bookstore.controller.admin;

import com.bookstore.bookstore.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final OrderService orderService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", bookService.count());
        model.addAttribute("totalCategories", categoryService.count());
        model.addAttribute("totalCustomers", userService.countCustomers());
        model.addAttribute("totalOrders", orderService.countByStatus("DELIVERED"));
        model.addAttribute("totalRevenue", orderService.calculateTotalRevenue());
        model.addAttribute("pendingOrders", orderService.countByStatus("PENDING"));
        model.addAttribute("recentOrders", orderService.getRecentOrders());
        return "admin/dashboard";
    }
}
