package com.da2.socialmedia.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "friend")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String status;

    @ManyToOne
    @JoinColumn(name = "matk1", nullable = false)
    private UserEntity user1;

    @ManyToOne
    @JoinColumn(name = "matk2", nullable = false)
    private UserEntity user2;
}
