package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.OrderEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderDetailController {

    private final OrderService orderService;

    @Autowired
    public OrderDetailController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderCode}")
    public String viewOrderDetail(@PathVariable String orderCode,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = currentUser.getUser();
        OrderEntity order = orderService.getOrderByCodeAndUser(orderCode, user);

        if (order == null) {
            return "redirect:/orders?error=order_not_found";
        }

        model.addAttribute("order", order);
        return "shop/chitiethoadon";
    }

    @GetMapping("")
    public String viewAllOrders(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = currentUser.getUser();
        model.addAttribute("orders", orderService.getAllOrdersByUser(user));
        return "shop/chitietdonhang";
    }

    @PostMapping("/cancel/{orderCode}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @PathVariable String orderCode,
            @RequestBody Map<String, String> cancelRequest,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Map<String, Object> response = new HashMap<>();

        try {
            String reason = cancelRequest.get("reason");
            if (reason == null || reason.trim().isEmpty()) {
                throw new IllegalArgumentException("Vui lòng cung cấp lý do hủy đơn hàng");
            }

            User user = currentUser.getUser();
            boolean success = orderService.cancelOrder(orderCode, user, reason);

            if (success) {
                response.put("success", true);
                response.put("message", "Đơn hàng đã được hủy thành công");
            } else {
                response.put("success", false);
                response.put("message", "Không thể hủy đơn hàng này");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/trahang/{id}")
    public String traHang(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal CustomUserDetails currentUser ) {
        orderService.setOrderItemDaTra(id);
        String orderCode = orderService.getOrderItemById(id).getOrder().getOrderCode();

        return "redirect:/orders/" + orderCode;

    }
}