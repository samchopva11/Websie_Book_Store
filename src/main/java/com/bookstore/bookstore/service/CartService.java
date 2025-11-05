package com.bookstore.bookstore.service;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.CartItem;
import com.bookstore.bookstore.entity.User;
import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(User user);
    CartItem addToCart(User user, Book book, Integer quantity);
    CartItem updateQuantity(Long cartItemId, Integer quantity);
    void removeFromCart(Long cartItemId);
    void clearCart(User user);
    long countCartItems(User user);
    BigDecimal getCartTotal(User user);
}
