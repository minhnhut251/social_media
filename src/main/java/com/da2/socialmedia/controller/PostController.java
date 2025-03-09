package com.da2.socialmedia.controller;

import com.da2.socialmedia.CustomUserDetails;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.service.LikeService;
import com.da2.socialmedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PostController {

    private final PostService postService;
    private final LikeService likeService;

    @Autowired
    public PostController(PostService postService, LikeService likeService) {
        this.postService = postService;
        this.likeService = likeService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<PostEntity> posts = postService.getAllPosts();

        // If user is authenticated, add like information for each post
        if (currentUser != null) {
            User user = currentUser.getUser();
            Map<Long, Boolean> likedPosts = new HashMap<>();
            Map<Long, Long> likeCounts = new HashMap<>();

            for (PostEntity post : posts) {
                likedPosts.put(post.getMabd(), likeService.hasUserLikedPost(user, post));
                likeCounts.put(post.getMabd(), likeService.countLikesForPost(post));
            }

            model.addAttribute("likedPosts", likedPosts);
            model.addAttribute("likeCounts", likeCounts);
        }

        model.addAttribute("listPosts", posts);
        return "index";
    }

    @GetMapping("/new_post")
    public String showNewPostPage(Model model) {
        model.addAttribute("post", new PostEntity());
        return "new-post";
    }

    @PostMapping("/add_post")
    public String addUser(PostEntity post, @RequestParam(value = "image", required = false) MultipartFile file, @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = currentUser.getUser();
        postService.createPost(post, user, file);
        return "redirect:/";
    }

    @GetMapping("/post_edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        PostEntity post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "edit-post";
    }

    @PostMapping("/update_post/{id}")
    public String updateUser(@PathVariable("id") long id, PostEntity post,
                             @RequestParam(value = "image", required = false) MultipartFile file,
                             Model model) {
        postService.updatePost(id, post, file);
        return "redirect:/";
    }

    @GetMapping("/post_delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        postService.deletePost(id);
        return "redirect:/";
    }
}