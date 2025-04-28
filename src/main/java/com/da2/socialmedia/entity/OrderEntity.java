package com.da2.socialmedia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderCode;
    private String status; // PENDING, PROCESSING, SHIPPING, COMPLETED, CANCELLED
    private Date createdAt;
    private Date confirmedAt;
    private Date shippingAt;
    private Date completedAt;
    private Date cancelledAt;

    private double subtotal;
    private double shippingFee;
    private double discount;
    private double total;

    private String paymentMethod; // COD, BANKING, MOMO
    private boolean isPaid;

    private String note;
    private String cancelReason;

    private String shippingProvider;
    private String trackingNumber;
    private Date expectedDeliveryDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Bỏ comment hoặc xóa đoạn này nếu không sử dụng
     @ManyToOne
     @JoinColumn(name = "address_id", nullable = false)
     private AddressEntity address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItemEntity> items = new ArrayList<>();

    @Transient
    private List<StoreGroup> storeGroups = new ArrayList<>();

    // Inner class for store grouping in UI
    @Data
    public static class StoreGroup {
        private Long id;
        private String name;
        private List<OrderItemEntity> items = new ArrayList<>();

        public StoreGroup(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}