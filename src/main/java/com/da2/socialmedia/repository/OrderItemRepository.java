package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.OrderEntity;
import com.da2.socialmedia.entity.OrderItemEntity;
import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {

    @Query("SELECT oi FROM OrderItemEntity oi WHERE oi.product.tkbh.matkbh = :vendorId")
    List<OrderItemEntity> findAllByVendorId(@Param("vendorId") Long vendorId);

//    List<OrderItemEntity> findByOrder(OrderEntity order);
}
