package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.TaiKhoanBanHangEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.PostService;
import com.da2.socialmedia.service.PostViewService;
import com.da2.socialmedia.service.TKBHService;
import com.da2.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class TKBHController {

    private final TKBHService tkbhService;

    @Autowired
    public TKBHController(TKBHService tkbhService) {
        this.tkbhService = tkbhService;
    }

    @GetMapping("/dangkybanhang")
    public String showRegistrationForm(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {

        // Check if the user already has a vendor account
        TaiKhoanBanHangEntity existingVendorAccount = tkbhService.findByUser(currentUser.getUser());

        if (existingVendorAccount != null) {
            // If vendor account exists, redirect to vendor page
            return "redirect:/vendor";
        }

        model.addAttribute("tkbh", new TaiKhoanBanHangEntity());

        return "shop/dangkybanhang";
    }

    @PostMapping("/process_register_tkbh")
    public String processRegister(TaiKhoanBanHangEntity taiKhoanBanHang, @AuthenticationPrincipal CustomUserDetails currentUser) {
        taiKhoanBanHang.setUser(currentUser.getUser());
        tkbhService.createTkbh(taiKhoanBanHang);

        return "redirect:/vendor";
    }


}