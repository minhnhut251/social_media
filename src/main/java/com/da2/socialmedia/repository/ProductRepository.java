package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.SanphamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<SanphamEntity, Long> {

    List<SanphamEntity> findByTkbhMatkbh(Long maTkbh);

    List<SanphamEntity> findByTenspContainingIgnoreCase(String keyword);
}