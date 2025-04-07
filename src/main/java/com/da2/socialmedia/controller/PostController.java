package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.*;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Controller
public class PostController {

    private final PostService postService;
    private final PostViewService postViewService;
    private final CommentService commentService;
    private final TKBHService tkbhService;
    private final ProductService productService;

    @Autowired
    public PostController(PostService postService, PostViewService postViewService ,CommentService commentService, TKBHService tkbhService, ProductService productService) {
        this.postService = postService;
        this.postViewService = postViewService;
        this.commentService = commentService;
        this.tkbhService = tkbhService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<PostEntity> posts = postService.getAllPosts();
        postViewService.preparePostsForDisplay(model, posts, currentUser);

        model.addAttribute("newpost", new PostEntity());

        // Check if the user has a vendor account
        if (currentUser != null) {
            boolean hasVendorAccount = tkbhService.findByUser(currentUser.getUser()) != null;
            model.addAttribute("hasVendorAccount", hasVendorAccount);
        }

        return "index";
    }

    @GetMapping("/post/{id}")
    public String showPostDetail(@PathVariable("id") long id, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        PostEntity post = postService.getPostById(id);
        postViewService.preparePostForDisplay(model, post, currentUser);

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

        return "posts/post-detail";
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
        return "posts/new-post";
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
        return "posts/edit-post";
    }

    @PostMapping("/update_post/{id}")
    public String updatePost(@PathVariable("id") long id, PostEntity post,
                             @RequestParam(value = "image", required = false) MultipartFile file,
                             Model model) {
        postService.updatePost(id, post, file);
        return "redirect:/";
    }

    @GetMapping("/post_delete/{id}")
    public String deletePost(@PathVariable("id") long id, Model model) {
        postService.deletePost(id);
        return "redirect:/";
    }

    // Add these methods to your PostController.java

    @GetMapping("/new_livestream")
    public String showNewLivestreamPage(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("post", new PostEntity());

        // Check if the user has a vendor account and add products if they do
        if (currentUser != null) {
            TaiKhoanBanHangEntity vendorAccount = tkbhService.findByUser(currentUser.getUser());
            if (vendorAccount != null) {
                List<SanphamEntity> products = productService.getProductsByTkbhMatkbh(vendorAccount.getMatkbh());
                model.addAttribute("products", products);
            }
            model.addAttribute("hasVendorAccount", vendorAccount != null);
        }

        return "live/new-livestream";
    }

    @PostMapping("/add_livestream")
    public String addLivestream(PostEntity post,
                                @RequestParam(value = "productId", required = false) Long productId,
                                @AuthenticationPrincipal CustomUserDetails currentUser) {
        User user = currentUser.getUser();

        // Set post type to LIVESTREAM
        post.setLoaiBaiDang(PostEntity.postType.LIVESTREAM);

        // If a product is selected, set it in the post
        if (productId != null) {
            SanphamEntity product = productService.getProductById(productId);
            post.setSanPham(product);
        }

        // Create the post without an image file
        postService.createPost(post, user, null);

        return "redirect:/";
    }

    @GetMapping("/livestream_edit/{id}")
    public String showLivestreamUpdateForm(@PathVariable("id") long id, Model model,
                                           @AuthenticationPrincipal CustomUserDetails currentUser) {
        PostEntity post = postService.getPostById(id);
        model.addAttribute("post", post);

        // Check if the user has a vendor account and add products if they do
        if (currentUser != null) {
            TaiKhoanBanHangEntity vendorAccount = tkbhService.findByUser(currentUser.getUser());
            if (vendorAccount != null) {
                List<SanphamEntity> products = productService.getProductsByTkbhMatkbh(vendorAccount.getMatkbh());
                model.addAttribute("products", products);
            }
        }

        return "live/edit-livestream";
    }

    @PostMapping("/update_livestream/{id}")
    public String updateLivestream(@PathVariable("id") long id,
                                   PostEntity post,
                                   @RequestParam(value = "productId", required = false) Long productId,
                                   Model model) {
        // Get the existing post
        PostEntity existingPost = postService.getPostById(id);

        // Update post content
        existingPost.setNoiDung(post.getNoiDung());

        // Update associated product if provided
        if (productId != null) {
            SanphamEntity product = productService.getProductById(productId);
            existingPost.setSanPham(product);
        } else {
            existingPost.setSanPham(null);
        }

        // Save the updated post
        postService.savePost(existingPost);

        return "redirect:/";
    }
}