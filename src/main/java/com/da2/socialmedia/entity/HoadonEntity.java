package com.da2.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "hoadon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoadonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mahd;
    private double tong;
    private int status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "matk", nullable = false)
    private User user;
}
