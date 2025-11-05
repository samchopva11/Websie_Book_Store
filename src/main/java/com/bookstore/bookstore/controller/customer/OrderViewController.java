package com.bookstore.bookstore.controller.customer;

import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderViewController {

    private final OrderService orderService;

    @GetMapping
    public String listOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        var orders = orderService.findOrdersByUser(user.getId());
        model.addAttribute("orders", orders);

        return "customer/orders";
    }

    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        var order = orderService.findById(id);

        // Check if order belongs to user (security check)
        if (!order.getUser().getId().equals(user.getId()) && !"ADMIN".equals(user.getRole())) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "customer/order-detail";
    }
}
