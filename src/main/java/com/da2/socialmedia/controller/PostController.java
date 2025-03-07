package com.da2.socialmedia.controller;

import com.da2.socialmedia.CustomUserDetails;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.PostRepository;

import com.da2.socialmedia.entity.PostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class PostController {

    private final String uploadDir = "./uploads/images";

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    public PostController(PostRepository postRepository){
        this.postRepository = postRepository;

        // Create the uploads directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
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
    public String addUser(PostEntity post, @RequestParam(value = "image", required = false) MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
        User user = customUser.getUser();

        post.setUsers(user);

        // Handle image upload
        if (file != null && !file.isEmpty()) {
            try {
                // Generate a unique filename
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID().toString() + fileExtension;

                // Save the file to the upload directory
                Path filePath = Paths.get(uploadDir, newFilename);
                Files.write(filePath, file.getBytes());

                // Store the relative path in the database
                post.setMediaURL("/uploads/images/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
    public String updateUser(@PathVariable("id") long id, PostEntity post,
                             @RequestParam(value = "image", required = false) MultipartFile file,
                             Model model) {
        PostEntity existing = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        existing.setNoiDung(post.getNoiDung());

        // Handle image update
        if (file != null && !file.isEmpty()) {
            try {
                // Delete the old image if it exists
                if (existing.getMediaURL() != null && !existing.getMediaURL().isEmpty()) {
                    // Extract the filename from the path
                    String oldFilePath = existing.getMediaURL();
                    String oldFileName = oldFilePath.substring(oldFilePath.lastIndexOf("/") + 1);
                    File oldFile = new File(uploadDir, oldFileName);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                // Generate a unique filename
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = UUID.randomUUID().toString() + fileExtension;

                // Save the file to the upload directory
                Path filePath = Paths.get(uploadDir, newFilename);
                Files.write(filePath, file.getBytes());

                // Store the relative path in the database
                existing.setMediaURL("/uploads/images/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        postRepository.save(existing);

        return "redirect:/";
    }

    @GetMapping("/post_delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        PostEntity post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Delete the image file if it exists
        if (post.getMediaURL() != null && !post.getMediaURL().isEmpty()) {
            // Extract the filename from the path
            String filePath = post.getMediaURL();
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            File file = new File(uploadDir, fileName);
            if (file.exists()) {
                file.delete();
            }
        }

        postRepository.delete(post);

        return "redirect:/";
    }
}