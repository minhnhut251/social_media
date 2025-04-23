package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.PostRepository;
import jakarta.transaction.Transactional;
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

//    private final String uploadDir = "./uploads/images";
    private final PostRepository postRepository;
    private final LikeService likeService;
    private final FileService fileService;

    @Autowired
    public PostService(PostRepository postRepository, LikeService likeService, FileService fileService) {
        this.postRepository = postRepository;
        this.likeService = likeService;
        this.fileService = fileService;

        // Create the uploads directory if it doesn't exist
//        File directory = new File(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
    }

    public List<PostEntity> getAllPosts() {
        return postRepository.findByOrderByCreatedAtDesc();
    }

    public PostEntity getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id: " + id));
    }

//    public void createPost(PostEntity post, User user, MultipartFile file) {
//        post.setUsers(user);
//
//        if (file != null && !file.isEmpty()) {
////            String mediaUrl = handleFileUpload(file);
//            String mediaUrl = fileService.handleFileUpload(file);
//            post.setMediaURL(mediaUrl);
//        }
//
//        postRepository.save(post);
//    }
public void createPost(PostEntity post, User user, MultipartFile file) {
    post.setUsers(user);

    if (post.getLoaiBaiDang() != PostEntity.postType.LIVESTREAM) {
        if (file != null && !file.isEmpty()) {
            // Get the file path
            String mediaUrl = fileService.handleFileUpload(file);
            post.setMediaURL(mediaUrl);

            // Determine the file type based on content type
            String contentType = file.getContentType();
            if (contentType != null) {
                if (contentType.startsWith("video/")) {
                    post.setLoaiBaiDang(PostEntity.postType.VIDEO);
                } else if (contentType.startsWith("image/")) {
                    post.setLoaiBaiDang(PostEntity.postType.IMAGE);
                }
            }
        } else {
            // If no file is uploaded, it's a TEXT post
            post.setLoaiBaiDang(PostEntity.postType.TEXT);
        }
    }

    postRepository.save(post);
}

//    public void updatePost(Long id, PostEntity postDetails, MultipartFile file) {
//        PostEntity existingPost = getPostById(id);
//        existingPost.setNoiDung(postDetails.getNoiDung());
//
//        if (file != null && !file.isEmpty()) {
//            // Delete old file if exists
////            deleteFileIfExists(existingPost.getMediaURL());
//            fileService.deleteFileIfExists(existingPost.getMediaURL());
//
//            // Upload new file
////            String mediaUrl = handleFileUpload(file);
//            String mediaUrl = fileService.handleFileUpload((file));
//            existingPost.setMediaURL(mediaUrl);
//        }
//
//        postRepository.save(existingPost);
//    }

    public void updatePost(Long id, PostEntity postDetails, MultipartFile file) {
        PostEntity existingPost = getPostById(id);
        existingPost.setNoiDung(postDetails.getNoiDung());

        if (file != null && !file.isEmpty()) {
            fileService.deleteFileIfExists(existingPost.getMediaURL());
            String mediaUrl = fileService.handleFileUpload(file);
            existingPost.setMediaURL(mediaUrl);

            // Update type
            String contentType = file.getContentType();
            if (contentType != null) {
                if (contentType.startsWith("video/")) {
                    existingPost.setLoaiBaiDang(PostEntity.postType.VIDEO);
                } else if (contentType.startsWith("image/")) {
                    existingPost.setLoaiBaiDang(PostEntity.postType.IMAGE);
                } else {
                    existingPost.setLoaiBaiDang(PostEntity.postType.TEXT);
                }
            }
        }


        postRepository.save(existingPost);
    }

    @Transactional
    public void deletePost(PostEntity post) {
        postRepository.delete(post);  // Xóa bài viết
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }




    public List<PostEntity> getAllVideosPosts() {
        return postRepository.findByLoaiBaiDang(PostEntity.postType.VIDEO);
    }

//    save an updated post for livestream
    public void savePost(PostEntity post) {
        postRepository.save(post);
    }

    public void deletePost(Long id) {
        PostEntity post = getPostById(id);

        // Delete associated file if exists
//        deleteFileIfExists(post.getMediaURL());
        fileService.deleteFileIfExists(post.getMediaURL());
        postRepository.delete(post);
    }

    public boolean hasUserLikedPost(User user, PostEntity post) {
        return likeService.hasUserLikedPost(user, post);
    }

    public long getLikeCount(PostEntity post) {
        return likeService.countLikesForPost(post);
    }

    // Helper methods for file operations
//    private String handleFileUpload(MultipartFile file) {
//        try {
//            // Generate a unique filename
//            String originalFilename = file.getOriginalFilename();
//            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
//            String newFilename = UUID.randomUUID().toString() + fileExtension;
//
//            // Save the file to the upload directory
//            Path filePath = Paths.get(uploadDir, newFilename);
//            Files.write(filePath, file.getBytes());
//
//            // Return the relative path
//            return "/uploads/images/" + newFilename;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to upload file", e);
//        }
//    }

//    private void deleteFileIfExists(String mediaURL) {
//        if (mediaURL != null && !mediaURL.isEmpty()) {
//            // Extract the filename from the path
//            String fileName = mediaURL.substring(mediaURL.lastIndexOf("/") + 1);
//            File file = new File(uploadDir, fileName);
//            if (file.exists()) {
//                file.delete();
//            }
//        }
//    }

    // Add these methods to your existing PostService class:

    /**
     * Get posts by user ID
     * @param userId The user ID
     * @return List of posts by the user
     */
    public List<PostEntity> getPostsByUserId(Long userId) {
        return postRepository.findByUsersIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Search posts by content
     * @param query The search query
     * @return List of matching posts
     */
    public List<PostEntity> searchPosts(String query) {
        return postRepository.findByNoiDungContainingIgnoreCaseOrderByCreatedAtDesc(query);
    }
}