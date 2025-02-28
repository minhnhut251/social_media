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
    private int matk1;
    private int matk2;
    private String status;
}
