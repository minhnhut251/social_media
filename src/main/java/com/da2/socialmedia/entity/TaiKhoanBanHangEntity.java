package com.da2.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;
@Entity
@Table(name = "taikhoanbanhang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanBanHangEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matk;
    private int mathkbh;
    private String ten;
    private String giayTo;
    private String avatar;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
