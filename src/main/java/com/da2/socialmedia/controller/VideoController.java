package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.PostRepository;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/video")
public class VideoController {
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @Autowired
//    private PostService postService;
//
//    @GetMapping("/upload")
//    public String showVideoUploadForm(Model model) {
//        model.addAttribute("post", new PostEntity());
//        return "video_upload";
//    }
//
//    @PostMapping("/upload")
//    public String uploadVideo(@ModelAttribute PostEntity post,
//                              @RequestParam("video") MultipartFile video,
//                              @AuthenticationPrincipal CustomUserDetails currentUser) {
//        User user = currentUser.getUser();
//        post.setLoaiBaiDang(PostEntity.postType.VIDEO); // Gán loại bài đăng là VIDEO
//        postService.createPost(post, user, video);
//        return "redirect:/video/watch";
//    }
//
//
//    @GetMapping("/watch")
//    public String viewVideoPage(Model model) {
//        model.addAttribute("videos", postService.getAllVideoPosts());
//        return "video_watch";
//    }
//
//
//    @GetMapping("/video/{id}")
//    public String videoDetail(@PathVariable("id") Long id, Model model) {
//        PostEntity post = postRepository.findById(id).orElse(null);
//        if (post == null || post.getLoaiBaiDang() != PostEntity.postType.VIDEO) {
//            return "redirect:/"; // Nếu không phải video hoặc không tồn tại thì quay về trang chủ
//        }
//        model.addAttribute("videoPost", post);
//        return "video_detail"; // Trang xem chi tiết video
//    }

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/video/{id}")
    public String viewVideo(@PathVariable("id") Long postId, Model model) {
        Optional<PostEntity> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {
            PostEntity post = optionalPost.get();
            if (post.getLoaiBaiDang() == PostEntity.postType.VIDEO) {
                model.addAttribute("post", post);
                return "video"; // Tên file HTML trong /templates
            } else {
                return "redirect:/"; // Nếu không phải video thì quay lại trang chủ
            }
        } else {
            return "redirect:/"; // Không tìm thấy bài viết thì quay lại trang chủ
        }
    }
//    @GetMapping("/video/{id}")
//    public String videoDetail(@PathVariable("id") Long id, Model model) {
//        PostEntity post = postRepository.findById(id).orElse(null);
//        if (post == null || post.getLoaiBaiDang() != PostEntity.postType.VIDEO) {
//            return "redirect:/"; // Nếu không phải video hoặc không tồn tại thì quay về trang chủ
//        }
//        model.addAttribute("videoPost", post);
//        return "video_detail"; // Trang xem chi tiết video
//    }
}
