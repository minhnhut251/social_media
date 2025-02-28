package com.da2.socialmedia.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "like")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int mabd;
    private String tenTK;
}
