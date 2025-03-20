package com.da2.socialmedia.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chitiethoadon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietHoaDonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int macthd;
    private int masp;
    private int sl;

    @ManyToOne
    @JoinColumn(name = "mahd", nullable = true)
    private HoadonEntity hoadon;
}
