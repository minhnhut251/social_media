package com.da2.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matb;
    private String noiDung;
    private String type;
    private int matkSender;
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "matk", nullable = false)
    private User user;
}
