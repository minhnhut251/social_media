package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.OrderEntity;
import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findByOrderCodeAndUser(String orderCode, User user);
    List<OrderEntity> findAllByUserOrderByCreatedAtDesc(User user);
}
