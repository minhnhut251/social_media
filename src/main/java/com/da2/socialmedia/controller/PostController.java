package com.da2.socialmedia.controller;

import com.da2.socialmedia.CustomUserDetails;
import com.da2.socialmedia.entity.CommentEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.service.PostService;
import com.da2.socialmedia.service.PostViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.da2.socialmedia.service.CommentService;


import java.util.List;

@Controller
public class PostController {

    private final PostService postService;
    private final PostViewService postViewService;
    private final CommentService commentService;

    @Autowired
    public PostController(PostService postService, PostViewService postViewService ,CommentService commentService) {
        this.postService = postService;
        this.postViewService = postViewService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<PostEntity> posts = postService.getAllPosts();
        postViewService.preparePostsForDisplay(model, posts, currentUser);
        return "index";
    }

    // New method to view a single post
//    @GetMapping("/post/{id}")
//    public String viewSinglePost(@PathVariable("id") long id, Model model,
//                                 @AuthenticationPrincipal CustomUserDetails currentUser) {
//        PostEntity post = postService.getPostById(id);
//        postViewService.preparePostForDisplay(model, post, currentUser);
//        return "post-detail";
//    }

    // New method to view user profile with their posts
//    @GetMapping("/user/{userId}")
//    public String viewUserPosts(@PathVariable("userId") long userId, Model model,
//                                @AuthenticationPrincipal CustomUserDetails currentUser) {
//        List<PostEntity> userPosts = postService.getPostsByUserId(userId);
//        postViewService.preparePostsForDisplay(model, userPosts, currentUser);
//
//        // Add username or other user info if needed
//         model.addAttribute("profileUser", currentUser.getUser());
//
//        return "taikhoan/personal-page";
//    }
    @GetMapping("/post/{id}")
    public String showPostDetail(@PathVariable("id") long id, Model model) {
        PostEntity post = postService.getPostById(id);
        if (post == null) {
            return "redirect:/";
        }

        // Get comments for this post
        List<CommentEntity> comments = commentService.getCommentsForPost(post);
        // Add to model
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);

        // Add any other attributes your view needs
        // e.g. Like counts, etc.

        return "post-detail";
    }

    // New method for search results
    @GetMapping("/search")
    public String searchPosts(@RequestParam("query") String query, Model model,
                              @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<PostEntity> searchResults = postService.searchPosts(query);
        postViewService.preparePostsForDisplay(model, searchResults, currentUser);
        model.addAttribute("searchQuery", query);
        return "posts/search-results";
    }

    @GetMapping("/new_post")
    public String showNewPostPage(Model model) {
        model.addAttribute("post", new PostEntity());
        return "new-post";
    }

    @PostMapping("/add_post")
    public String addUser(PostEntity post, @RequestParam(value = "image", required = false) MultipartFile file,
                          @AuthenticationPrincipal CustomUserDetails currentUser) {
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