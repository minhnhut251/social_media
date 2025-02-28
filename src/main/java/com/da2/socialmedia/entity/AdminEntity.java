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
    private int matkadmin;
    private String matkhau;
    private String email;
    private int maoyen;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "mavt", nullable = false)
    private RoleEntity role;
}
