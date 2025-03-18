package com.da2.socialmedia.controller;

import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.service.PostService;
import com.da2.socialmedia.service.PostViewService;
import com.da2.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final PostViewService postViewService;

    @Autowired
    public UserController(UserService userService, PostService postService, PostViewService postViewService) {
        this.userService = userService;
        this.postService = postService;
        this.postViewService = postViewService;
    }

    @GetMapping("/user/{userId}")
    public String viewUserProfile(@PathVariable("userId") long userId, Model model,
                                  @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            // Get user profile
            User profileUser = userService.getUserById(userId);
            model.addAttribute("profileUser", profileUser);

            // Get user posts
            var userPosts = postService.getPostsByUserId(userId);
            postViewService.preparePostsForDisplay(model, userPosts, currentUser);

            // Check if current user is viewing their own profile
            boolean isOwnProfile = currentUser != null && currentUser.getUser().getId() == userId;
            model.addAttribute("isOwnProfile", isOwnProfile);

            return "taikhoan/personal-page";
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }
    }

    @GetMapping("/profile/edit")
    public String showEditProfileForm(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        User user = userService.getUserById(currentUser.getId());
        model.addAttribute("user", user);
        return "taikhoan/edit-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user,
                                @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null || !currentUser.getUser().getId().equals(user.getId())) {
            return "redirect:/";
        }

        userService.updateUserProfile(user);
        return "redirect:/user/" + user.getId();
    }

    @GetMapping("/vendor")
    public String vendorPage(){
        return "shop/vendor";
    }

//    @PostMapping("/profile/update-avatar")
//    public String updateAvatar(@RequestParam("avatar") MultipartFile file,
//                               @AuthenticationPrincipal CustomUserDetails currentUser) {
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//
//        userService.updateUserAvatar(currentUser.getUser().getId(), file);
//        return "redirect:/user/" + currentUser.getUser().getId();
//    }
//
//    @PostMapping("/profile/update-banner")
//    public String updateBanner(@RequestParam("banner") MultipartFile file,
//                               @AuthenticationPrincipal CustomUserDetails currentUser) {
//        if (currentUser == null) {
//            return "redirect:/login";
//        }
//
//        userService.updateUserBanner(currentUser.getUser().getId(), file);
//        return "redirect:/user/" + currentUser.getUser().getId();
//    }
}