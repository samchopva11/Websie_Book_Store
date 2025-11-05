package com.bookstore.bookstore.controller.customer;

import com.bookstore.bookstore.entity.Order;
import com.bookstore.bookstore.entity.OrderItem;
import com.bookstore.bookstore.entity.User;
import com.bookstore.bookstore.service.CartService;
import com.bookstore.bookstore.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigDecimal;
import java.util.ArrayList;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    public String showCheckoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        var cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }

        var cartTotal = cartService.getCartTotal(user);

        // Calculate shipping fee (free if > 500k)
        BigDecimal shippingFee = cartTotal.compareTo(BigDecimal.valueOf(500000)) >= 0
                ? BigDecimal.ZERO
                : BigDecimal.valueOf(30000);

        BigDecimal grandTotal = cartTotal.add(shippingFee);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartTotal", cartTotal);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("user", user);

        return "customer/checkout";
    }

    @PostMapping("/place-order")
    public String placeOrder(
            @RequestParam String receiverName,
            @RequestParam String shippingPhone,
            @RequestParam String shippingAddress,
            @RequestParam(required = false) String note,
            @RequestParam String paymentMethod,
            HttpSession session,
            RedirectAttributes ra) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            var cartItems = cartService.getCartItems(user);
            if (cartItems.isEmpty()) {
                ra.addFlashAttribute("error", "Giỏ hàng trống!");
                return "redirect:/cart";
            }

            // Create order
            Order order = new Order();
            order.setReceiverName(receiverName);
            order.setShippingPhone(shippingPhone);
            order.setShippingAddress(shippingAddress);
            order.setNote(note);
            order.setPaymentMethod(paymentMethod);

            // Create order items from cart
            var orderItems = new ArrayList<OrderItem>();
            for (var cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setBook(cartItem.getBook());
                orderItem.setBookTitle(cartItem.getBook().getTitle());
                orderItem.setBookPrice(cartItem.getBook().getPrice());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setSubtotal(
                        cartItem.getBook().getPrice()
                                .multiply(BigDecimal.valueOf(cartItem.getQuantity()))
                );
                orderItems.add(orderItem);
            }
            order.setOrderItems(orderItems);

            // Save order
            Order savedOrder = orderService.createOrder(order, user);

            // Clear cart
            cartService.clearCart(user);

            ra.addFlashAttribute("success", "Đặt hàng thành công! Mã đơn hàng: " + savedOrder.getOrderNumber());
            return "redirect:/orders/" + savedOrder.getId();

        } catch (Exception e) {
            ra.addFlashAttribute("error", "Đặt hàng thất bại: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/success")
    public String checkoutSuccess(Model model) {
        return "customer/checkout-success";
    }
}
