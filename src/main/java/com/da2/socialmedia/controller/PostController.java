package com.da2.socialmedia.controller;

import com.da2.socialmedia.CustomUserDetails;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.PostRepository;

import com.da2.socialmedia.entity.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    @Autowired
    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<PostEntity> listPosts = postRepository.findAll();
        model.addAttribute("listPosts", listPosts);
        return "index";
    }

    @GetMapping("/new_post")
    public String showNewPostPage(Model model) {
        model.addAttribute("post", new PostEntity());
        return "new-post";
    }

    @PostMapping("/add_post")
    public String addUser(PostEntity post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
        User user = customUser.getUser();

        post.setUsers(user);

        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/post_edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        PostEntity post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("post", post);

        return "edit-post";
    }

    @PostMapping("/update_post/{id}")
    public String updateUser(@PathVariable("id") long id, PostEntity post, Model model) {
        PostEntity existing = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        existing.setNoiDung(post.getNoiDung());
        postRepository.save(existing);

        return "redirect:/";
    }

    @GetMapping("/post_delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        PostEntity post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        postRepository.delete(post);

        return "redirect:/";
    }
}
