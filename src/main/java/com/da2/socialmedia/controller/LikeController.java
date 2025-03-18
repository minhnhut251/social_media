package com.da2.socialmedia.controller;

import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.service.LikeService;
import com.da2.socialmedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    private final LikeService likeService;
    private final PostService postService;

    @Autowired
    public LikeController(LikeService likeService, PostService postService) {
        this.likeService = likeService;
        this.postService = postService;
    }

    @PostMapping("/post/{postId}/like")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {

        // Get the current user and post
        User user = currentUser.getUser();
        PostEntity post = postService.getPostById(postId);

        // Toggle the like status
        boolean isLiked = likeService.toggleLike(user, post);

        // Get the updated like count
        long likeCount = likeService.countLikesForPost(post);

        // Prepare response data
        Map<String, Object> response = new HashMap<>();
        response.put("liked", isLiked);
        response.put("likeCount", likeCount);

        return ResponseEntity.ok(response);
    }
}