//package com.da2.socialmedia.entity;
//import jakarta.persistence.*;
//import lombok.*;
//@Entity
//@Table(name = "friend")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class FriendEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int mabn;
//
//    private String status;
//
//    @ManyToOne
//    @JoinColumn(name = "matk1", nullable = false)
//    private User user1;
//
//    @ManyToOne
//    @JoinColumn(name = "matk2", nullable = false)
//    private User user2;
//}
package com.da2.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friends")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "matk1", nullable = false)
    private User user1; // Người gửi lời mời

    @ManyToOne
    @JoinColumn(name = "matk2", nullable = false)
    private User user2; // Người nhận lời mời

    @Column(name = "status")
    private String status; // "Pending", "Accepted", "Rejected"

    public FriendEntity(User user1, User user2, String status) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
    }
}
