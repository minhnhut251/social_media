package com.da2.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mabl;
    private String noiDung;
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "matk", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "mabd", nullable = false)
    private PostEntity post;
}
