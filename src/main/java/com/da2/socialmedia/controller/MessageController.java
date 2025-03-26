package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.MessageEntity;
import com.da2.socialmedia.entity.PostEntity;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class MessageController {
    @Autowired
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService){
        this.messageService = messageService;
        this.userService = userService;
    }

    @GetMapping("/messenger")
    public String messengerPage(@AuthenticationPrincipal CustomUserDetails currentUser, Model model){
        List<User> listUsers = userService.getAllUsers();
        model.addAttribute("listUsers", listUsers);

        return("messenger/messenger-index");
    }

    @GetMapping("/messenger/{userId}")
    public String conversationPage(@AuthenticationPrincipal CustomUserDetails currentUser, Model model, @PathVariable("userId") long userId){

        User nguoiNhan = userService.getUserById(userId);
        User nguoiGui = userService.getUserById(currentUser.getId());

        List<MessageEntity> conversation = messageService.getConversation(nguoiNhan, nguoiGui);

        model.addAttribute("nguoiNhan", nguoiNhan);
        model.addAttribute("nguoiGui", nguoiGui);
        model.addAttribute("conversation", conversation);

        model.addAttribute("new_message", new MessageEntity());

        return("messenger/conversation");
    }


    @PostMapping("/add_message")
    public String thenTinNhan(MessageEntity message,
                          @AuthenticationPrincipal CustomUserDetails currentUser,
                          @RequestParam(name = "nguoinhan") Long idNguoiNhan) {
        System.out.println(idNguoiNhan);
        User nguoiGui = currentUser.getUser();
        User nguoiNhan = userService.getUserById(idNguoiNhan);
        messageService.createMessage(message, nguoiGui, nguoiNhan);
        return "redirect:/messenger/" + idNguoiNhan;
    }
}
