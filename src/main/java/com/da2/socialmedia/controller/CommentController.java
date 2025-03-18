package com.da2.socialmedia.controller;


import com.da2.socialmedia.entity.CommentEntity;
import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.CommentService;
import com.da2.socialmedia.service.PostService;
import com.da2.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.Instant;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/post/{postId}/comment")
    public String addComment(@PathVariable("postId") long postId,
                             @RequestParam("commentContent") String content,
                             @AuthenticationPrincipal CustomUserDetails currentUser,
    RedirectAttributes redirectAttributes) {


        try {
            // Get current authenticated user
            User user = currentUser.getUser();

            // Get the post
            PostEntity post = postService.getPostById(postId);

            if (post == null) {
                redirectAttributes.addFlashAttribute("error", "Post not found");
                return "redirect:/";
            }

            // Create new comment
            CommentEntity comment = new CommentEntity();
            comment.setNoiDung(content);
            comment.setCreatedAt(Timestamp.from(Instant.now()));
            comment.setUser(user);
            comment.setPost(post);

            // Save comment
            commentService.saveComment(comment);

            redirectAttributes.addFlashAttribute("success", "Comment added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding comment: " + e.getMessage());
        }

        return "redirect:/post/" + postId;
    }

    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") int commentId,
                                @AuthenticationPrincipal CustomUserDetails currentUser,
                                RedirectAttributes redirectAttributes) {

        try {
            CommentEntity comment = commentService.getCommentById(commentId);

            if (comment == null) {
                redirectAttributes.addFlashAttribute("error", "Comment not found");
                return "redirect:/";
            }

            // Check if current user is the comment owner
            User user = currentUser.getUser();
            if (comment.getUser().getId() != currentUser.getId()) {
                redirectAttributes.addFlashAttribute("error", "You can only delete your own comments");
                return "redirect:/post/" + comment.getPost().getMabd();
            }

            // Delete comment
            commentService.deleteComment(commentId);

            redirectAttributes.addFlashAttribute("success", "Comment deleted successfully");
            return "redirect:/post/" + comment.getPost().getMabd();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting comment: " + e.getMessage());
            return "redirect:/";
        }
    }
}
