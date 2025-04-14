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
import java.util.stream.Collectors;

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

    public void prepareVideosForDisplay(Model model, List<PostEntity> posts, CustomUserDetails currentUser) {
        Map<Long, Boolean> likedPosts = new HashMap<>();
        Map<Long, Long> likeCounts = new HashMap<>();

        // Lọc các bài đăng có loại là video
        List<PostEntity> videoPosts = posts.stream()
                .filter(post -> PostEntity.postType.VIDEO.equals(post.getLoaiBaiDang()))  // Lọc bài đăng có loại video
                .collect(Collectors.toList());

        // Nếu người dùng đã đăng nhập, kiểm tra bài đăng nào đã được thích
        if (currentUser != null) {
            User user = currentUser.getUser();

            for (PostEntity post : videoPosts) {
                likedPosts.put(post.getMabd(), likeService.hasUserLikedPost(user, post));
                likeCounts.put(post.getMabd(), likeService.countLikesForPost(post));
            }
        } else {
            // Nếu người dùng chưa đăng nhập, chỉ lấy số lượt thích
            for (PostEntity post : videoPosts) {
                likedPosts.put(post.getMabd(), false);
                likeCounts.put(post.getMabd(), likeService.countLikesForPost(post));
            }
        }

        // Cập nhật dữ liệu vào model
        model.addAttribute("likedPosts", likedPosts);
        model.addAttribute("likeCounts", likeCounts);
        model.addAttribute("listVideos", videoPosts);  // Chỉ gửi bài đăng video vào model
    }

}