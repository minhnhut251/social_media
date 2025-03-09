package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private final String uploadDir = "./uploads/images";
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;

        // Create the uploads directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    public PostEntity getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id: " + id));
    }

    public void createPost(PostEntity post, User user, MultipartFile file) {
        post.setUsers(user);

        if (file != null && !file.isEmpty()) {
            String mediaUrl = handleFileUpload(file);
            post.setMediaURL(mediaUrl);
        }

        postRepository.save(post);
    }

    public void updatePost(Long id, PostEntity postDetails, MultipartFile file) {
        PostEntity existingPost = getPostById(id);
        existingPost.setNoiDung(postDetails.getNoiDung());

        if (file != null && !file.isEmpty()) {
            // Delete old file if exists
            deleteFileIfExists(existingPost.getMediaURL());

            // Upload new file
            String mediaUrl = handleFileUpload(file);
            existingPost.setMediaURL(mediaUrl);
        }

        postRepository.save(existingPost);
    }

    public void deletePost(Long id) {
        PostEntity post = getPostById(id);

        // Delete associated file if exists
        deleteFileIfExists(post.getMediaURL());

        postRepository.delete(post);
    }

    // Helper methods for file operations
    private String handleFileUpload(MultipartFile file) {
        try {
            // Generate a unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            // Save the file to the upload directory
            Path filePath = Paths.get(uploadDir, newFilename);
            Files.write(filePath, file.getBytes());

            // Return the relative path
            return "/uploads/images/" + newFilename;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    private void deleteFileIfExists(String mediaURL) {
        if (mediaURL != null && !mediaURL.isEmpty()) {
            // Extract the filename from the path
            String fileName = mediaURL.substring(mediaURL.lastIndexOf("/") + 1);
            File file = new File(uploadDir, fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}