package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.CartItemEntity;
import com.da2.socialmedia.entity.SanphamEntity;
import com.da2.socialmedia.entity.TaiKhoanBanHangEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    public List<CartItemEntity> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    public List<StoreGroup> getStoreGroups(User user) {
        List<CartItemEntity> cartItems = getCartItems(user);
        Map<TaiKhoanBanHangEntity, List<CartItemEntity>> groupedItems = cartItems.stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getTkbh()));

        List<StoreGroup> storeGroups = new ArrayList<>();
        groupedItems.forEach((store, items) -> {
            StoreGroup group = new StoreGroup();
            group.setId(store.getMatkbh());
            group.setName(store.getTenStore());
            group.setItems(items);
            storeGroups.add(group);
        });

        return storeGroups;
    }

    @Transactional
    public CartItemEntity addToCart(User user, Long productId, Integer quantity) {
        SanphamEntity product = productService.getProductById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        Optional<CartItemEntity> existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            CartItemEntity item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItemEntity newItem = new CartItemEntity(user, product, quantity);
            return cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public CartItemEntity updateCartItemQuantity(Long itemId, Integer quantity, User user) {
        CartItemEntity item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        // Ensure the cart item belongs to the current user
        if (!item.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not authorized to update this cart item");
        }

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Transactional
    public void removeFromCart(Long itemId, User user) {
        CartItemEntity item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        // Ensure the cart item belongs to the current user
        if (!item.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not authorized to remove this cart item");
        }

        cartItemRepository.delete(item);
    }

    @Transactional
    public void clearCart(User user) {
        List<CartItemEntity> userItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(userItems);
    }

    // Inner class for store grouping
    public static class StoreGroup {
        private Long id;
        private String name;
        private List<CartItemEntity> items;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CartItemEntity> getItems() {
            return items;
        }

        public void setItems(List<CartItemEntity> items) {
            this.items = items;
        }
    }
}