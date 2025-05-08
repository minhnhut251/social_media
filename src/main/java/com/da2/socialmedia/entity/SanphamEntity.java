package com.da2.socialmedia.entity;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "sanpham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanphamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long masp;
    private String tensp;
    private int sl;
    private double gia;
    private String img;
    private String moTa;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "matkbh", nullable = false)
    private TaiKhoanBanHangEntity tkbh;

    private boolean discontinued = false;
}
