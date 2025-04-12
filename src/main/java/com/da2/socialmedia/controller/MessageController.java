package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.MessageEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.MessageService;
import com.da2.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService){
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/messenger")
    public String messengerPage(@AuthenticationPrincipal CustomUserDetails currentUser, Model model){
        User currentUserEntity = userService.getUserById(currentUser.getId());

        // Get all users sorted by recent messages
        List<User> allUsers = userService.getAllUsers().stream()
                .filter(user -> user.getId() != currentUser.getId())
                .collect(Collectors.toList());

        // Get most recent conversations
        Map<Long, MessageEntity> lastMessages = new HashMap<>();
        List<User> sortedUsers = new ArrayList<>();

        for (User user : allUsers) {
            List<MessageEntity> conversation = messageService.getConversation(user, currentUserEntity);
            if (!conversation.isEmpty()) {
                // Get the most recent message
                MessageEntity lastMessage = conversation.stream()
                        .max(Comparator.comparing(MessageEntity::getCreatedAt))
                        .orElse(null);

                if (lastMessage != null) {
                    lastMessages.put(user.getId(), lastMessage);
                    sortedUsers.add(user);
                }
            }
        }

        // Sort users by most recent message
        sortedUsers.sort((u1, u2) -> {
            MessageEntity m1 = lastMessages.get(u1.getId());
            MessageEntity m2 = lastMessages.get(u2.getId());
            return m2.getCreatedAt().compareTo(m1.getCreatedAt());
        });

        // Add users with no messages at the end
        for (User user : allUsers) {
            if (!sortedUsers.contains(user)) {
                sortedUsers.add(user);
            }
        }

        model.addAttribute("sortedUsers", sortedUsers);
        model.addAttribute("lastMessages", lastMessages);

        // If there are users with conversations, redirect to the first one
        if (!sortedUsers.isEmpty()) {
            User firstUser = sortedUsers.get(0);
            return "redirect:/messenger/" + firstUser.getId();
        }

        return "messenger/messenger-index";
    }

    @GetMapping("/messenger/{userId}")
    public String conversationPage(@AuthenticationPrincipal CustomUserDetails currentUser, Model model, @PathVariable("userId") long userId){
        User currentUserEntity = userService.getUserById(currentUser.getId());
        User nguoiNhan = userService.getUserById(userId);

        // Get conversation
        List<MessageEntity> conversation = messageService.getConversation(nguoiNhan, currentUserEntity);

        // Get all users sorted by recent messages
        List<User> allUsers = userService.getAllUsers().stream()
                .filter(user -> user.getId() != currentUser.getId())
                .collect(Collectors.toList());

        // Get most recent conversations
        Map<Long, MessageEntity> lastMessages = new HashMap<>();
        List<User> sortedUsers = new ArrayList<>();

        for (User user : allUsers) {
            List<MessageEntity> userConversation = messageService.getConversation(user, currentUserEntity);
            if (!userConversation.isEmpty()) {
                // Get the most recent message
                MessageEntity lastMessage = userConversation.stream()
                        .max(Comparator.comparing(MessageEntity::getCreatedAt))
                        .orElse(null);

                if (lastMessage != null) {
                    lastMessages.put(user.getId(), lastMessage);
                    sortedUsers.add(user);
                }
            }
        }

        // Sort users by most recent message
        sortedUsers.sort((u1, u2) -> {
            MessageEntity m1 = lastMessages.get(u1.getId());
            MessageEntity m2 = lastMessages.get(u2.getId());
            return m2.getCreatedAt().compareTo(m1.getCreatedAt());
        });

        // Add users with no messages at the end
        for (User user : allUsers) {
            if (!sortedUsers.contains(user)) {
                sortedUsers.add(user);
            }
        }

        model.addAttribute("sortedUsers", sortedUsers);
        model.addAttribute("lastMessages", lastMessages);
        model.addAttribute("nguoiNhan", nguoiNhan);
        model.addAttribute("nguoiGui", currentUserEntity);
        model.addAttribute("conversation", conversation);
        model.addAttribute("new_message", new MessageEntity());
        model.addAttribute("selectedUserId", userId);

        return "messenger/messenger-index";
    }

    @PostMapping("/add_message")
    public String thenTinNhan(MessageEntity message,
                              @AuthenticationPrincipal CustomUserDetails currentUser,
                              @RequestParam(name = "nguoinhan") Long idNguoiNhan) {
        User nguoiGui = currentUser.getUser();
        User nguoiNhan = userService.getUserById(idNguoiNhan);
        messageService.createMessage(message, nguoiGui, nguoiNhan);
        return "redirect:/messenger/" + idNguoiNhan;
    }
}