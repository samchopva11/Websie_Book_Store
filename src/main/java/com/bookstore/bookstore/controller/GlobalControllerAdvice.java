package com.bookstore.bookstore.controller;

import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CartService cartService;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        int cartCount = 0;

        if (user != null) {
            try {
                cartCount = cartService.getCartItems(user).size();
            } catch (Exception ignored) {}
        }

        model.addAttribute("cartCount", cartCount);
    }
}
