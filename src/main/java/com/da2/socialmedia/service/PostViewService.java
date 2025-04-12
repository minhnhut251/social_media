package com.da2.socialmedia.service;

import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostViewService {

    private final LikeService likeService;
    private final CommentService commentService; // Add this

    @Autowired
    public PostViewService(LikeService likeService, CommentService commentService) { // Update constructor
        this.likeService = likeService;
        this.commentService = commentService; // Initialize commentService
    }

    public void preparePostsForDisplay(Model model, List<PostEntity> posts, CustomUserDetails currentUser) {
        Map<Long, Boolean> likedPosts = new HashMap<>();
        Map<Long, Long> likeCounts = new HashMap<>();
        Map<Long, Long> commentCounts = new HashMap<>(); // Add this map for comment counts

        // If user is authenticated, check which posts they have liked
        if (currentUser != null) {
            User user = currentUser.getUser();

            for (PostEntity post : posts) {
                likedPosts.put(post.getMabd(), likeService.hasUserLikedPost(user, post));
                likeCounts.put(post.getMabd(), likeService.countLikesForPost(post));
                commentCounts.put(post.getMabd(), commentService.countCommentsForPost(post)); // Add this
            }
        } else {
            // If not authenticated
            for (PostEntity post : posts) {
                likedPosts.put(post.getMabd(), false);
                likeCounts.put(post.getMabd(), likeService.countLikesForPost(post));
                commentCounts.put(post.getMabd(), commentService.countCommentsForPost(post)); // Add this
            }
        }

        model.addAttribute("likedPosts", likedPosts);
        model.addAttribute("likeCounts", likeCounts);
        model.addAttribute("commentCounts", commentCounts); // Add this to the model
        model.addAttribute("listPosts", posts);
    }

    public void preparePostForDisplay(Model model, PostEntity post, CustomUserDetails currentUser) {
        Map<Long, Boolean> likedPosts = new HashMap<>();
        Map<Long, Long> likeCounts = new HashMap<>();
        Map<Long, Long> commentCounts = new HashMap<>(); // Add this

        // If user is authenticated, check if they have liked this post
        if (currentUser != null) {
            User user = currentUser.getUser();
            likedPosts.put(post.getMabd(), likeService.hasUserLikedPost(user, post));
        } else {
            likedPosts.put(post.getMabd(), false);
        }

        likeCounts.put(post.getMabd(), likeService.countLikesForPost(post));
        commentCounts.put(post.getMabd(), commentService.countCommentsForPost(post)); // Add this

        model.addAttribute("likedPosts", likedPosts);
        model.addAttribute("likeCounts", likeCounts);
        model.addAttribute("commentCounts", commentCounts); // Add this to the model
        model.addAttribute("post", post);
    }
}