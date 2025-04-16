package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.AddressEntity;
import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByUser(User user);
}