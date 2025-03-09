package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.LikeEntity;
import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    /**
     * Toggle like status for a post
     * @param user The user liking/unliking the post
     * @param post The post to like/unlike
     * @return true if the post is now liked, false if it was unliked
     */
    public boolean toggleLike(User user, PostEntity post) {
        // Check if the user has already liked the post
        var existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            // User already liked the post, so unlike it
            likeRepository.delete(existingLike.get());
            return false;
        } else {
            // User hasn't liked the post, so like it
            LikeEntity like = new LikeEntity();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            return true;
        }
    }

    /**
     * Check if a user has liked a post
     * @param user The user
     * @param post The post
     * @return true if the user has liked the post, false otherwise
     */
    public boolean hasUserLikedPost(User user, PostEntity post) {
        return likeRepository.existsByUserAndPost(user, post);
    }

    /**
     * Count the number of likes for a post
     * @param post The post
     * @return The number of likes
     */
    public long countLikesForPost(PostEntity post) {
        return likeRepository.countByPost(post);
    }
}