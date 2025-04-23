package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> findByOrderByCreatedAtDesc();

    // Find posts by user ID
    List<PostEntity> findByUsersIdOrderByCreatedAtDesc(Long userId);

    // Search posts by content
    List<PostEntity> findByNoiDungContainingIgnoreCaseOrderByCreatedAtDesc(String query);

    // Add this to your PostRepository interface
    List<PostEntity> findByLoaiBaiDang(PostEntity.postType loaiBaiDang);

//    List<PostEntity> findByTrangThai(PostStatus trangThai);

}