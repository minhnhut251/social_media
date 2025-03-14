package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.CommentEntity;
import com.da2.socialmedia.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    // Find all comments for a specific post, ordered by creation time (newest first)
    List<CommentEntity> findByPostOrderByCreatedAtDesc(PostEntity post);

    // Count comments for a specific post
    long countByPost(PostEntity post);
}
