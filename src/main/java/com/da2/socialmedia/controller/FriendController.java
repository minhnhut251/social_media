package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.FriendEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.entity.UserFriendDTOEntity;
import com.da2.socialmedia.repository.FriendRepository;
import com.da2.socialmedia.repository.UserRepository;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.FriendService;
import com.da2.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;


@Controller
//@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @GetMapping("/friends")
    public String showSuggestions(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        User user = currentUser.getUser();

        // Lấy danh sách ID bạn bè đã kết bạn
        List<Long> friendIds = friendRepository.findConfirmedFriendIds(user.getId());

        // Lấy danh sách người dùng chưa kết bạn

        List<User> suggestedUsers = userRepository.findUsersNotInFriendList(user.getId(), friendIds);

        List<UserFriendDTOEntity> suggestionDTOs = new ArrayList<>();

        for (User suggested : suggestedUsers) {
            Optional<FriendEntity> friendship = friendRepository.findFriendship(user, suggested);
            String status = friendship.map(FriendEntity::getStatus).orElse(null);
            suggestionDTOs.add(new UserFriendDTOEntity(suggested, status));
        }

//        model.addAttribute("suggestedUsers", suggestedUsers);
//        return "/friend/friends";

        model.addAttribute("suggestedUsers", suggestionDTOs);
        return "/friend/friends";
    }



    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    // Gửi lời mời kết bạn
    @GetMapping("/add-friend")
    public String sendFriendRequest(@AuthenticationPrincipal CustomUserDetails currentUser,
                                    @RequestParam("id") Long friendId,
                                    RedirectAttributes redirectAttributes) {
        User sender = currentUser.getUser();
        boolean success = friendService.sendFriendRequest(sender, friendId);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Đã gửi lời mời kết bạn!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Lời mời kết bạn đã tồn tại!");
        }

        return "redirect:/friends";
    }

    // Chấp nhận lời mời kết bạn
//    @GetMapping("/accept-friend")
//    public String acceptFriendRequest(@RequestParam("id") Long requestId, RedirectAttributes redirectAttributes) {
//        boolean success = friendService.acceptFriendRequest(requestId);
//        if (success) {
//            redirectAttributes.addFlashAttribute("successMessage", "Bạn đã chấp nhận lời mời kết bạn!");
//        } else {
//            redirectAttributes.addFlashAttribute("errorMessage", "Lời mời kết bạn không tồn tại!");
//        }
//        return "redirect:/friend-request";
//    }
    @GetMapping("/accept-friend")
    public String acceptFriendRequest(@RequestParam("id") Long requestId, RedirectAttributes redirectAttributes) {
        boolean success = friendService.acceptFriendRequest(requestId);
        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Bạn đã chấp nhận lời mời kết bạn!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Lời mời kết bạn không tồn tại!");
        }
        return "redirect:/friend-requests"; // ✅ đúng route
    }


    // Từ chối lời mời kết bạn
//    @GetMapping("/reject-friend")
//    public String rejectFriendRequest(@RequestParam("id") Long requestId, RedirectAttributes redirectAttributes) {
//        boolean success = friendService.rejectFriendRequest(requestId);
//        Map<String, Integer> response = new HashMap<>();
//        if (success) {
//            redirectAttributes.addFlashAttribute("successMessage", "Bạn đã từ chối lời mời kết bạn!");
//        } else {
//            redirectAttributes.addFlashAttribute("errorMessage", "Lời mời kết bạn không tồn tại!");
//        }
//        return "redirect:/friend-requests";
//    }
    @GetMapping("/reject-friend")
    @ResponseBody
    public Map<String, Object> rejectFriendRequest(@RequestParam("id") Long requestId) {
        Map<String, Object> response = new HashMap<>();
        boolean success = friendService.rejectFriendRequest(requestId);

        if (success) {
            response.put("success", true);
            response.put("message", "Bạn đã từ chối lời mời kết bạn!");
        } else {
            response.put("success", false);
            response.put("message", "Lời mời kết bạn không tồn tại!");
        }

        return response;
    }

    // Hiển thị danh sách lời mời kết bạn
//    @GetMapping("/friend-requests")
//    public String showFriendRequests(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
//        User user = currentUser.getUser();
//        List<FriendEntity> pendingRequests = friendService.getPendingRequests(user);
//        model.addAttribute("pendingRequests", pendingRequests);
//        return "/friend_requests";
//    }
    @GetMapping("friend-requests")
    public String showFriendRequests(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        User user = currentUser.getUser();
        List<FriendEntity> pendingRequests = friendService.getPendingRequests(user);

        model.addAttribute("pendingRequests", pendingRequests);
        return "/friend/friend_requests";
    }


    @GetMapping("list")
    public String friendList(@AuthenticationPrincipal CustomUserDetails currentUser, Model model) {
        User user = userService.getUserById(currentUser.getId());
        List<User> friends = friendService.getFriends(user);
        model.addAttribute("friends", friends);
        return "/friend/friend_list";
    }
//    @GetMapping("/list")
//    public String viewlistfriend() {
//
//        return "/friend/friend_list";
//    }
//    @GetMapping("/friend-requests")
//    public String viewrequest() {
//
//        return "/friend/friend_requests";
//    }
    @GetMapping("/unfriend")
    public String unfriend(@AuthenticationPrincipal CustomUserDetails currentUser,
                           @RequestParam("id") Long friendId,
                           RedirectAttributes redirectAttributes) {
        User user = currentUser.getUser();
        boolean success = friendService.unfriend(user, friendId);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Bạn đã hủy kết bạn thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể hủy kết bạn!");
        }

        return "redirect:/list"; // Chuyển hướng về danh sách bạn bè
    }

}
