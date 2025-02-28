package com.da2.socialmedia.entity;

import java.sql.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int matk;
    private String tentk;
    private String matkhau;
    private String hoten;
    private String email;
    private int sdt;
    private String avatar;
    private Date ngaysinh;
    private String banner;
    private String sex;
    private Timestamp createdAt;
    private Timestamp updatedAt;

//    @OneToMany(mappedBy = "users")
//    private List<Post> posts;
//
//    @OneToMany(mappedBy = "users")
//    private List<Comment> comments;
}
