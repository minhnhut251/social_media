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
    private int mattb;
    private int matk;
    private String noiDung;
    private String type;
    private int matkSender;
    private Timestamp createdAt;
}
