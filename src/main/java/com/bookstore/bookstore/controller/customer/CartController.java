package com.bookstore.bookstore.controller.customer;

import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.BookService;
import com.bookstore.bookstore.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final BookService bookService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("cartItems", cartService.getCartItems(user));
        model.addAttribute("cartTotal", cartService.getCartTotal(user));
        return "customer/cart";
    }

    @PostMapping("/add")
    public String addToCart(
            @RequestParam Long bookId,
            @RequestParam(defaultValue = "1") Integer quantity,
            HttpSession session,
            RedirectAttributes ra) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            var book = bookService.findById(bookId);
            cartService.addToCart(user, book, quantity);
            ra.addFlashAttribute("success", "Đã thêm vào giỏ hàng!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/update/{id}")
    public String updateCart(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            RedirectAttributes ra) {

        try {
            cartService.updateQuantity(id, quantity);
            ra.addFlashAttribute("success", "Đã cập nhật giỏ hàng!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, RedirectAttributes ra) {
        cartService.removeFromCart(id);
        ra.addFlashAttribute("success", "Đã xóa khỏi giỏ hàng!");
        return "redirect:/cart";
    }
}
