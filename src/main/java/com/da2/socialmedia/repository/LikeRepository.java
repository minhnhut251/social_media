package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.LikeEntity;
import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {

    // Find if a user has liked a specific post
    Optional<LikeEntity> findByUserAndPost(User user, PostEntity post);

    // Count the number of likes for a post
    long countByPost(PostEntity post);

    // Check if a user has liked a post
    boolean existsByUserAndPost(User user, PostEntity post);
}