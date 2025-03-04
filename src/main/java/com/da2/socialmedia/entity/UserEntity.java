//package com.da2.socialmedia.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//
////
////import java.sql.Timestamp;
////import jakarta.persistence.*;
////import lombok.*;
////import java.util.Date;
////import java.util.List;
////
////@Entity
////@Table(name = "users")
////@Data
////@NoArgsConstructor
////@AllArgsConstructor
////@Getter
////@Setter
//public class UserEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, unique = true, length = 45)
//    private String email;
//
//    @Column(nullable = false, length = 64)
//    private String password;
//
//    @Column(name = "first_name", nullable = false, length = 20)
//    private String firstName;
//
//    @Column(name = "last_name", nullable = false, length = 20)
//    private String lastName;
//
//
//
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private int matk;
////    private String tentk;
////    private String matkhau;
////    private String hoten;
////    private String email;
////    private int sdt;
////    private String avatar;
////    private Date ngaysinh;
////    private String banner;
////    private String sex;
////    private Timestamp createdAt;
////    private Timestamp updatedAt;
////
////    @OneToMany(mappedBy = "users")
////    private List<Post> posts;
////
////    @OneToMany(mappedBy = "users")
////    private List<Comment> comments;
//}
