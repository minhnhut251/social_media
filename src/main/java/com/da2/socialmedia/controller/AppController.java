package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/admin/login")
    public String viewAdminLoginPage() {
        return "admin/admin_login";
    }

    @GetMapping({"/admin", "/admin/"})
    public String viewAdminHomePage() {
        return "admin/admin_home";
    }

//    @GetMapping("/friends")
//    public String viewFriend() {
//
//        return "/friends";
//    }

    @GetMapping("/carts")
    public String viewcart() {

        return "/shop/cart";
    }

//    @GetMapping("/shop")
//    public String viewshop() {
//
//        return "/shop/shop";
//    }

    @GetMapping("/video")
    public String viewvideo() {

        return "/video";
    }

//    @GetMapping("")
//    public String viewHomePage() {
//        return "index";
//    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "taikhoan/signup_form";
    }

    @GetMapping("/login")
    String login() {
        return "taikhoan/login";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "taikhoan/register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userRepo.findAll();
        model.addAttribute("listUsers", listUsers);

        return "taikhoan/users";
    }


}
