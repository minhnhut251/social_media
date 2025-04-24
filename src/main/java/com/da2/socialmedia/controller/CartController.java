package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.CartItemEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.CartService;
import com.da2.socialmedia.service.AddressService;
import com.da2.socialmedia.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final AddressService addressService;

    @Autowired
    public CartController(CartService cartService, ProductService productService, AddressService addressService) {
        this.cartService = cartService;
        this.productService = productService;
        this.addressService = addressService;
    }

    @PostMapping("/add-address")
    @ResponseBody
    public Map<String, Object> addAddress(@RequestParam Map<String, String> addressData,
                                          @AuthenticationPrincipal CustomUserDetails currentUser) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Gọi phương thức đã thêm vào AddressService
            addressService.saveAddress(currentUser.getUser(), addressData);

            response.put("success", true);
            response.put("message", "Địa chỉ đã được thêm thành công");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    @GetMapping("/checkout")
    public String checkoutPage(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = currentUser.getUser();
        List<CartItemEntity> cartItems = cartService.getCartItems(user);

        // Kiểm tra giỏ hàng trống
        if (cartItems.isEmpty()) {
            model.addAttribute("error", "Giỏ hàng của bạn đang trống!");
            return "redirect:/cart";
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("addresses", addressService.getUserAddresses(user));

        return "shop/checkout";
    }

    @GetMapping("")
    public String viewCart(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = currentUser.getUser();
        List<CartItemEntity> cartItems = cartService.getCartItems(user);
        List<CartService.StoreGroup> storeGroups = cartService.getStoreGroups(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("storeGroups", storeGroups);

        return "shop/giohang";
    }

    @GetMapping("/count")
    @ResponseBody
    public Map<String, Integer> getCartCount(@AuthenticationPrincipal CustomUserDetails currentUser) {
        Map<String, Integer> response = new HashMap<>();
        int count = 0;

        if (currentUser != null) {
            count = cartService.getCartItemCount(currentUser.getUser());
        }
        response.put("count", count);
        return response;
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<Map<String, Object>> addToCart(
            @PathVariable Long productId,
            @RequestBody(required = false) Map<String, Object> requestBody,
            @RequestParam(required = false) Integer quantity,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Get quantity from either request body or request param
            Integer actualQuantity = quantity;
            if (requestBody != null && requestBody.containsKey("quantity")) {
                actualQuantity = Integer.valueOf(requestBody.get("quantity").toString());
            }

            // Default to 1 if not specified
            if (actualQuantity == null) {
                actualQuantity = 1;
            }

            CartItemEntity cartItem = cartService.addToCart(currentUser.getUser(), productId, actualQuantity);
            response.put("success", true);
            response.put("message", "Sản phẩm đã được thêm vào giỏ hàng");
            response.put("cartItem", cartItem.getId());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

//    @PostMapping("/add/{productId}")
//    public ResponseEntity<Map<String, Object>> addToCart(
//            @PathVariable Long productId,
//            @RequestParam(defaultValue = "1") Integer quantity,
//            @AuthenticationPrincipal CustomUserDetails currentUser) {
//
//        Map<String, Object> response = new HashMap<>();
//
//        try {
//            CartItemEntity cartItem = cartService.addToCart(currentUser.getUser(), productId, quantity);
//            response.put("success", true);
//            response.put("message", "Sản phẩm đã được thêm vào giỏ hàng");
//            response.put("cartItem", cartItem.getId());
//        } catch (Exception e) {
//            response.put("success", false);
//            response.put("message", e.getMessage());
//        }
//
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/update/{itemId}")
    public ResponseEntity<Map<String, Object>> updateCartItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, Integer> requestBody,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Map<String, Object> response = new HashMap<>();

        try {
            Integer quantity = requestBody.get("quantity");
            if (quantity == null || quantity < 1) {
                throw new IllegalArgumentException("Invalid quantity");
            }

            cartService.updateCartItemQuantity(itemId, quantity, currentUser.getUser());
            response.put("success", true);
            response.put("message", "Số lượng đã được cập nhật");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove/{itemId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Map<String, Object> response = new HashMap<>();

        try {
            cartService.removeFromCart(itemId, currentUser.getUser());
            response.put("success", true);
            response.put("message", "Sản phẩm đã được xóa khỏi giỏ hàng");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    // Phần này nên được tích hợp với CheckoutController hoặc sử dụng CheckoutService
    // Loại bỏ phần POST /checkout trùng lặp và thay thế bằng redirect đến CheckoutController
}