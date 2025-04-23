package com.da2.socialmedia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

//@Entity
//@Table(name = "taikhoanbanhang")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "taikhoanbanhang")
@Getter
@Setter
@ToString(exclude = "user")  // Loại bỏ user khỏi toString()
@EqualsAndHashCode(exclude = "user")  // Loại bỏ user khỏi hashCode và equals
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanBanHangEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matkbh;
    private String tenStore;
    private String giayTo;
    private String gioiThieu;
    private String diaChi;
    private String avatar;

    @Column(columnDefinition = "TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "matk", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private VendorStatus status = VendorStatus.PENDING_APPROVAL;

    public enum VendorStatus {
        PENDING_APPROVAL,
        ACTIVE,
        SUSPENDED
    }

    private double wallet = 0;
}
