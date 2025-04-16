package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.CartItemEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.AddressService;
import com.da2.socialmedia.service.CartService;
import com.da2.socialmedia.service.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Random;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final CheckoutService checkoutService;
    private final AddressService addressService;

    @Autowired
    public CheckoutController(CartService cartService, CheckoutService checkoutService, AddressService addressService) {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
        this.addressService = addressService;
    }

    @GetMapping("")
    public String checkout(Model model, @AuthenticationPrincipal CustomUserDetails currentUser, RedirectAttributes redirectAttributes) {
        User user = currentUser.getUser();

        // Kiểm tra xem giỏ hàng có trống không
        List<CartItemEntity> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống!");
            return "redirect:/cart";
        }

        // Lấy các địa chỉ của người dùng
        model.addAttribute("addresses", addressService.getUserAddresses(user));

        // Nhóm sản phẩm theo cửa hàng
        List<CartService.StoreGroup> storeGroups = cartService.getStoreGroups(user);
        model.addAttribute("storeGroups", storeGroups);

        // Tính toán tổng tiền
        double subtotal = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getGia() * item.getQuantity())
                .sum();
        double shippingFee = 30000; // Phí cố định hoặc tính toán dựa trên logic của bạn
        double discount = 0; // Tính giảm giá nếu có
        double total = subtotal + shippingFee - discount;

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("discount", discount);
        model.addAttribute("total", total);

        // Tạo mã đơn hàng tạm thời
        String orderCode = generateOrderCode();
        model.addAttribute("orderCode", orderCode);

        return "shop/thanhtoan";
    }

    @PostMapping("/confirm")
    public String confirmOrder(
            @RequestParam("addressId") Long addressId,
            @RequestParam("paymentMethod") String paymentMethod,
            @RequestParam("note") String note,
            @RequestParam("subtotal") double subtotal,
            @RequestParam("shippingFee") double shippingFee,
            @RequestParam("discount") double discount,
            @RequestParam("total") double total,
            @RequestParam("itemIds") List<Long> itemIds,
            @RequestParam Map<String, String> allParams,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            RedirectAttributes redirectAttributes) {

        User user = currentUser.getUser();

        try {
            // Tạo đơn hàng mới
            String orderCode = checkoutService.createOrder(
                    user, addressId, itemIds, allParams, paymentMethod,
                    note, subtotal, shippingFee, discount, total
            );

            // Xóa các mục đã đặt hàng khỏi giỏ hàng
            checkoutService.removeOrderedItemsFromCart(user, itemIds);

            redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công! Mã đơn hàng của bạn là: " + orderCode);
            return "redirect:/orders/" + orderCode;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    private String generateOrderCode() {
        // Tạo mã đơn hàng, ví dụ: DH + timestamp + 4 số ngẫu nhiên
        long timestamp = System.currentTimeMillis();
        int randomNum = new Random().nextInt(10000);
        return "DH" + timestamp % 10000 + String.format("%04d", randomNum);
    }
}