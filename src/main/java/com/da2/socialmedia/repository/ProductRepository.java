package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.SanphamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<SanphamEntity, Integer> {

    List<SanphamEntity> findByUsersId(Long userId);

    List<SanphamEntity> findByTenspContainingIgnoreCase(String keyword);
}