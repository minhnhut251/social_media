package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.TaiKhoanBanHangEntity;
import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TKBHRepository extends JpaRepository<TaiKhoanBanHangEntity, Long> {
    @Query("SELECT t FROM TaiKhoanBanHangEntity t WHERE t.user = :user")
    TaiKhoanBanHangEntity findByUser(@Param("user") User user);
}
