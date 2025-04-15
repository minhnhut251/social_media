package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.TaiKhoanBanHangEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.service.PostService;
import com.da2.socialmedia.service.TKBHService;
import com.da2.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final TKBHService tkbhService;
    private final UserService userService;
    private final PostService postService;



    @Autowired
    public AdminDashboardController(TKBHService tkbhService,UserService userService,PostService postService) {
        this.tkbhService = tkbhService;
        this.userService=userService;
        this.postService=postService;
    }

    @GetMapping("login")
    public String viewAdminLoginPage() {
        return "admin/admin_login";
    }

    @GetMapping({"/", ""})
    public String viewAdminHomePage() {
        return "admin/admin_main";
    }

//    @GetMapping({"/admin/tkbh", "/admin/"})
//    public String viewAdminTKBH() {
//        return "admin/admin_tkbh";
//    }

    @GetMapping("/users")
    public String showUsersPage(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin_user"; // File Thymeleaf trong thư mục templates/admin/
    }

    @GetMapping("/posts")
    public String showPostPage(Model model) {
        List<PostEntity> postEntities = postService.getAllPosts();
        model.addAttribute("posts", postEntities);
        return "admin/admin_post"; // File Thymeleaf trong thư mục templates/admin/
    }


    @GetMapping("/tkbh")
    public String showTKBHPage(Model model) {
        List<TaiKhoanBanHangEntity> vendors = tkbhService.findAllVendors();
        model.addAttribute("vendors", vendors);
        return "admin/admin_tkbh";
    }

    @GetMapping("/tkbh/filter")
    public String filterVendors(@RequestParam(required = false) String status, Model model) {
        List<TaiKhoanBanHangEntity> vendors;

        if (status != null && !status.isEmpty()) {
            TaiKhoanBanHangEntity.VendorStatus vendorStatus = TaiKhoanBanHangEntity.VendorStatus.valueOf(status);
            vendors = tkbhService.findVendorsByStatus(vendorStatus);
        } else {
            vendors = tkbhService.findAllVendors();
        }

        model.addAttribute("vendors", vendors);
        return "admin/fragments/vendor_table :: vendorTable";
    }

    @GetMapping("/tkbh/{id}")
    @ResponseBody
    public ResponseEntity<?> getVendorDetails(@PathVariable Long id) {
        Optional<TaiKhoanBanHangEntity> vendor = tkbhService.findById(id);

        if (vendor.isPresent()) {
            return ResponseEntity.ok(vendor.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Không tìm thấy tài khoản bán hàng");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/tkbh/update-status")
    @ResponseBody
    public ResponseEntity<?> updateVendorStatus(
            @RequestParam Long vendorId,
            @RequestParam String status) {

        Map<String, Object> response = new HashMap<>();

        try {
            TaiKhoanBanHangEntity.VendorStatus vendorStatus = TaiKhoanBanHangEntity.VendorStatus.valueOf(status);
            boolean updated = tkbhService.updateVendorStatus(vendorId, vendorStatus);

            if (updated) {
                response.put("success", true);
                response.put("message", "Cập nhật trạng thái thành công");
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy tài khoản bán hàng");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}