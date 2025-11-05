package com.bookstore.bookstore.controller.admin;

import com.bookstore.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public String listOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            Model model) {

        int pageSize = 10;
        var pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        var ordersPage = (status != null && !status.isEmpty())
                ? orderService.findByStatus(status, pageable)
                : (search != null && !search.isEmpty())
                ? orderService.searchOrders(search, pageable)
                : orderService.findAll(pageable);

        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("currentStatus", status);
        model.addAttribute("searchKeyword", search);
        model.addAttribute("currentPage", page);

        // Statistics
        model.addAttribute("pendingCount", orderService.countByStatus("PENDING"));
        model.addAttribute("confirmedCount", orderService.countByStatus("CONFIRMED"));
        model.addAttribute("shippingCount", orderService.countByStatus("SHIPPING"));
        model.addAttribute("deliveredCount", orderService.countByStatus("DELIVERED"));
        model.addAttribute("cancelledCount", orderService.countByStatus("CANCELLED"));

        return "admin/orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model) {
        var order = orderService.findById(id);
        model.addAttribute("order", order);
        return "admin/orders/detail";
    }

    @PostMapping("/update-status/{id}")
    public String updateOrderStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes ra) {

        try {
            orderService.updateOrderStatus(id, status);
            ra.addFlashAttribute("success", "Đã cập nhật trạng thái đơn hàng!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/orders/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes ra) {
        try {
            orderService.deleteById(id);
            ra.addFlashAttribute("success", "Đã xóa đơn hàng!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa đơn hàng: " + e.getMessage());
        }
        return "redirect:/admin/orders";
    }
}
