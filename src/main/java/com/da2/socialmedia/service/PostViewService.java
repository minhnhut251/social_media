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

/**
 * Service responsible for preparing post data for view templates
 */
@Service
public class PostViewService {

    private final LikeService likeService;

    @Autowired
    public PostViewService(LikeService likeService) {
        this.likeService = likeService;
    }

    /**
     * Prepares post data for display with like information
     * @param model The model to add attributes to
     * @param posts The list of posts to prepare
     * @param currentUser The current user (can be null if not authenticated)
     */
    public void preparePostsForDisplay(Model model, List<PostEntity> posts, CustomUserDetails currentUser) {
        Map<Long, Boolean> likedPosts = new HashMap<>();
        Map<Long, Long> likeCounts = new HashMap<>();

        // If user is authenticated, check which posts they have liked
        if (currentUser != null) {
            User user = currentUser.getUser();

            for (PostEntity post : posts) {
                likedPosts.put(post.getMabd(), likeService.hasUserLikedPost(user, post));
                likeCounts.put(post.getMabd(), likeService.countLikesForPost(post));
            }
        } else {
            // If not authenticated, just get like counts
            for (PostEntity post : posts) {
                likedPosts.put(post.getMabd(), false);
                likeCounts.put(post.getMabd(), likeService.countLikesForPost(post));
            }
        }

        model.addAttribute("likedPosts", likedPosts);
        model.addAttribute("likeCounts", likeCounts);
        model.addAttribute("listPosts", posts);
    }

    /**
     * Prepares a single post for display with like information
     * @param model The model to add attributes to
     * @param post The post to prepare
     * @param currentUser The current user (can be null if not authenticated)
     */
    public void preparePostForDisplay(Model model, PostEntity post, CustomUserDetails currentUser) {
        Map<Long, Boolean> likedPosts = new HashMap<>();
        Map<Long, Long> likeCounts = new HashMap<>();

        // If user is authenticated, check if they have liked this post
        if (currentUser != null) {
            User user = currentUser.getUser();
            likedPosts.put(post.getMabd(), likeService.hasUserLikedPost(user, post));
        } else {
            likedPosts.put(post.getMabd(), false);
        }

        likeCounts.put(post.getMabd(), likeService.countLikesForPost(post));

        model.addAttribute("likedPosts", likedPosts);
        model.addAttribute("likeCounts", likeCounts);
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