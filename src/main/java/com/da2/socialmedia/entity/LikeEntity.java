package com.da2.socialmedia.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "luotthich")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int malt;
    private String tenTK;

    @ManyToOne
    @JoinColumn(name = "matk", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "mabd", nullable = false)
    private PostEntity post;
}
