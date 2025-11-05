package com.bookstore.bookstore.service.impl;

import com.bookstore.bookstore.entity.Book;
import com.bookstore.bookstore.entity.CartItem;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.repository.CartItemRepository;
import com.bookstore.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    @Override
    public CartItem addToCart(User user, Book book, Integer quantity) {
        // Kiểm tra sách đã có trong giỏ chưa
        CartItem cartItem = cartItemRepository.findByUserAndBook(user, book)
                .orElse(new CartItem());

        if (cartItem.getId() == null) {
            // Item mới
            cartItem.setUser(user);
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);
        } else {
            // Cập nhật quantity
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        // Validate stock
        if (cartItem.getQuantity() > book.getStock()) {
            throw new RuntimeException("Số lượng vượt quá tồn kho!");
        }

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng!"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        // Validate stock
        if (quantity > cartItem.getBook().getStock()) {
            throw new RuntimeException("Số lượng vượt quá tồn kho!");
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    public void removeFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart(User user) {
        cartItemRepository.deleteByUserId(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public long countCartItems(User user) {
        return cartItemRepository.countByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getCartTotal(User user) {
        List<CartItem> cartItems = getCartItems(user);
        return cartItems.stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
