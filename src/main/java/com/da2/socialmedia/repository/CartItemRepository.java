package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.CartItemEntity;
import com.da2.socialmedia.entity.SanphamEntity;
import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByUser(User user);
    Optional<CartItemEntity> findByUserAndProduct(User user, SanphamEntity product);
    void deleteByUser(User user);
}