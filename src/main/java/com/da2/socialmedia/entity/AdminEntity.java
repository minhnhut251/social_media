package com.da2.socialmedia.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matk;
    private String matkhau;
    private String email;
    private int mavt;
    private int maoyen;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
