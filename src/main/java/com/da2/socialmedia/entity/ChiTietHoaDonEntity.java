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
    private int mahd;
    private int masp;
    private int sl;
}
