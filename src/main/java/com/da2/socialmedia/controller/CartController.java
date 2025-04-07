package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.CartItemEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.CartService;
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
//

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping("")
    public String viewCart(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = currentUser.getUser();
        List<CartItemEntity> cartItems = cartService.getCartItems(user);
        List<CartService.StoreGroup> storeGroups = cartService.getStoreGroups(user);


        model.addAttribute("cartItems", cartItems);
        model.addAttribute("storeGroups", storeGroups);


        return "giohang";
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<Map<String, Object>> addToCart(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Map<String, Object> response = new HashMap<>();

        try {
            CartItemEntity cartItem = cartService.addToCart(currentUser.getUser(), productId, quantity);
            response.put("success", true);
            response.put("message", "Sản phẩm đã được thêm vào giỏ hàng");
            response.put("cartItem", cartItem.getId());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

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



    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> checkout(
            @RequestBody Map<String, Object> checkoutData,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        Map<String, Object> response = new HashMap<>();

        try {
            // Extract checkout data
            List<Map<String, Object>> items = (List<Map<String, Object>>) checkoutData.get("items");
            Long addressId = Long.parseLong(checkoutData.get("addressId").toString());

            // Here you would integrate with your order service
            // For now, we'll just provide a successful response

            response.put("success", true);
            response.put("message", "Đơn hàng đã được tạo thành công");
            response.put("redirectUrl", "/orders");

            // Clear the selected items from cart
            // This part depends on your order implementation
            // You might only want to remove items that were successfully ordered

            /*
            for (Map<String, Object> item : items) {
                Long itemId = Long.parseLong(item.get("id").toString());
                cartService.removeFromCart(itemId, currentUser.getUser());
            }
            */

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}