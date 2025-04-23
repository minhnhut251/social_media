package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.OrderEntity;
import com.da2.socialmedia.entity.OrderItemEntity;
import com.da2.socialmedia.entity.SanphamEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.OrderItemRepository;
import com.da2.socialmedia.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
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


//Lay tat ca order item cua 1 cua hang
    public List<OrderItemEntity> getOrderItemsByVendorId(Long vendorId) {
        return orderItemRepository.findAllByVendorId(vendorId);
    }

    public OrderItemEntity getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
    }

    public void setOrderItemDaGiao (Long orderItemId){
        OrderItemEntity orderItemEntity = getOrderItemById(orderItemId);
        orderItemEntity.setStatus(OrderItemEntity.OIStatus.DA_GIAO);
        orderItemRepository.save(orderItemEntity);
    }

    public void setOrderItemDaTra (Long orderItemId){
        OrderItemEntity orderItemEntity = getOrderItemById(orderItemId);
        orderItemEntity.setStatus(OrderItemEntity.OIStatus.DA_TRA);
        orderItemRepository.save(orderItemEntity);
    }


//    // Lay tat ca order item trong 1 order
//    public List<OrderItemEntity> getOrderItemsByOrder(OrderEntity order) {
//        return orderItemRepository.findByOrder(order);
//    }
}