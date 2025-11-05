package com.bookstore.bookstore.controller.admin;

import com.bookstore.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final UserService userService;

    @GetMapping
    public String listCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            Model model) {

        int pageSize = 10;
        var pageable = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        var customersPage = (search != null && !search.isEmpty())
                ? userService.searchCustomers(search, pageable)
                : userService.findCustomers(pageable);

        model.addAttribute("customersPage", customersPage);
        model.addAttribute("searchKeyword", search);
        model.addAttribute("currentPage", page);

        return "admin/customers/list";
    }

    @GetMapping("/{id}")
    public String viewCustomerDetail(@PathVariable Long id, Model model) {
        var customer = userService.findById(id);
        model.addAttribute("customer", customer);
        return "admin/customers/detail";
    }

    @PostMapping("/toggle-status/{id}")
    public String toggleCustomerStatus(@PathVariable Long id, RedirectAttributes ra) {
        try {
            var customer = userService.findById(id);
            customer.setIsActive(!customer.getIsActive());
            userService.save(customer);

            String status = customer.getIsActive() ? "kích hoạt" : "khóa";
            ra.addFlashAttribute("success", "Đã " + status + " tài khoản khách hàng!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/customers";
    }

    @PostMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userService.deleteById(id);
            ra.addFlashAttribute("success", "Đã xóa khách hàng!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa khách hàng: " + e.getMessage());
        }
        return "redirect:/admin/customers";
    }
}
