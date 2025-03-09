package com.da2.socialmedia.controller;

import com.da2.socialmedia.CustomUserDetails;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.entity.PostEntity;
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

@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listPosts", postService.getAllPosts());
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