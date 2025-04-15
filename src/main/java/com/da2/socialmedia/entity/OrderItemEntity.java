package com.da2.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


    @Entity
    @Table(name = "order_items")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class OrderItemEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private int quantity;
        private double price;
        private double total;

        @ManyToOne
        @JoinColumn(name = "order_id", nullable = false)
        private OrderEntity order;

        @ManyToOne
        @JoinColumn(name = "product_id", nullable = false)
        private SanphamEntity product;
    }

