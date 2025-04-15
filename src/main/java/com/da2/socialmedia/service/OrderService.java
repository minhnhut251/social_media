package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.OrderEntity;
import com.da2.socialmedia.entity.OrderItemEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderEntity getOrderByCodeAndUser(String orderCode, User user) {
        return orderRepository.findByOrderCodeAndUser(orderCode, user);
    }

    public List<OrderEntity> getAllOrdersByUser(User user) {
        return orderRepository.findAllByUserOrderByCreatedAtDesc(user);
    }

    @Transactional
    public boolean cancelOrder(String orderCode, User user, String reason) {
        OrderEntity order = orderRepository.findByOrderCodeAndUser(orderCode, user);

        if (order == null) {
            return false;
        }

        // Check if order can be canceled (only PENDING orders can be canceled)
        if (!order.getStatus().equals("PENDING")) {
            return false;
        }

        order.setStatus("CANCELLED");
        order.setCancelReason(reason);
        order.setCancelledAt(new Date());

        orderRepository.save(order);
        return true;
    }

    public static class StoreGroup {
        private Long storeId;
        private String name;
        private List<OrderItemEntity> items = new ArrayList<>();

        public StoreGroup(Long storeId, String name) {
            this.storeId = storeId;
            this.name = name;
        }

        public void addItem(OrderItemEntity item) {
            items.add(item);
        }

        // Getters and setters
        public Long getStoreId() {
            return storeId;
        }

        public void setStoreId(Long storeId) {
            this.storeId = storeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<OrderItemEntity> getItems() {
            return items;
        }

        public void setItems(List<OrderItemEntity> items) {
            this.items = items;
        }
    }
}