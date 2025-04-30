package com.da2.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mabd;
    private String noiDung;
    private String mediaURL;
//    private String sharePost;
    private boolean livestream_ended = false;

    @Column(columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "matk", nullable = false)
    private User users;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeEntity> likes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

    @Enumerated(EnumType.STRING)
    private postType loaiBaiDang = postType.TEXT;

    public enum postType {
        TEXT,
        IMAGE,
        VIDEO,
        LIVESTREAM
    }

    @ManyToOne
    @JoinColumn(name = "masp", nullable = true)
    private SanphamEntity sanPham;
}
