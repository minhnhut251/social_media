package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.OrderItemEntity;
import com.da2.socialmedia.entity.SanphamEntity;
import com.da2.socialmedia.entity.TaiKhoanBanHangEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class TKBHController {

    private final TKBHService tkbhService;
    private final FileService fileService;
    private final OrderService orderService;

    @Autowired
    public TKBHController(TKBHService tkbhService, FileService fileService, OrderService orderService) {
        this.tkbhService = tkbhService;
        this.fileService = fileService;
        this.orderService = orderService;
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
    public String processRegister(TaiKhoanBanHangEntity taiKhoanBanHang, @AuthenticationPrincipal CustomUserDetails currentUser,
                                  @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                                  @RequestParam(value = "giayToFile", required = false) MultipartFile giayToFile) {
        taiKhoanBanHang.setUser(currentUser.getUser());

        // Handle avatar file upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileService.handleFileUpload(avatarFile);
            taiKhoanBanHang.setAvatar(avatarUrl);
        }

        // Handle giayTo file upload
        if (giayToFile != null && !giayToFile.isEmpty()) {
            String giayToUrl = fileService.handleFileUpload(giayToFile);
            taiKhoanBanHang.setGiayTo(giayToUrl);
        }

        tkbhService.createTkbh(taiKhoanBanHang);

        return "redirect:/vendor";
    }

    @GetMapping("/vendor/edit")
    public String showEditVendorForm(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        TaiKhoanBanHangEntity vendorAccount = tkbhService.findByUser(currentUser.getUser());

        if (vendorAccount == null) {
            // If no vendor account exists, redirect to registration
            return "redirect:/dangkybanhang";
        }

        model.addAttribute("tkbh", vendorAccount);
        return "shop/edit-vendor";
    }

    @PostMapping("/vendor/update")
    public String updateVendorAccount(@ModelAttribute TaiKhoanBanHangEntity taiKhoanBanHang,
                                      @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                                      @RequestParam(value = "giayToFile", required = false) MultipartFile giayToFile,
                                      @AuthenticationPrincipal CustomUserDetails currentUser) {

        TaiKhoanBanHangEntity existingAccount = tkbhService.findByUser(currentUser.getUser());

        // Update fields
        existingAccount.setTenStore(taiKhoanBanHang.getTenStore());
        existingAccount.setGioiThieu(taiKhoanBanHang.getGioiThieu());
        existingAccount.setDiaChi(taiKhoanBanHang.getDiaChi());

        // Handle avatar file upload
        if (avatarFile != null && !avatarFile.isEmpty()) {
            // Delete old avatar if exists
            fileService.deleteFileIfExists(existingAccount.getAvatar());
            // Upload new avatar
            String avatarUrl = fileService.handleFileUpload(avatarFile);
            existingAccount.setAvatar(avatarUrl);
        }

        // Handle giayTo file upload
        if (giayToFile != null && !giayToFile.isEmpty()) {
            // Delete old giayTo if exists
            fileService.deleteFileIfExists(existingAccount.getGiayTo());
            // Upload new giayTo
            String giayToUrl = fileService.handleFileUpload(giayToFile);
            existingAccount.setGiayTo(giayToUrl);
        }

        tkbhService.updateTkbh(existingAccount);
        return "redirect:/vendor";
    }


    @GetMapping("/vendor/donhang")
    public String getVendorOrderItems(Model model, @AuthenticationPrincipal CustomUserDetails currentUser ) {
        TaiKhoanBanHangEntity vendorAccount = tkbhService.findByUser(currentUser.getUser());
        List<OrderItemEntity> orderItems = orderService.getOrderItemsByVendorId(vendorAccount.getMatkbh());

        model.addAttribute("orderItems", orderItems);


        return "shop/vendor-donhang";

    }

    @GetMapping("/vendor/donhang/update_donhang/{id}")
    public String setDaGiaoVendorOrderItems(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal CustomUserDetails currentUser ) {
        orderService.setOrderItemDaGiao(id);


        return "redirect:/vendor/donhang";

    }

    @GetMapping("/vendor/vitien")
    public String viTienTKBH(Model model, @AuthenticationPrincipal CustomUserDetails currentUser ) {
        TaiKhoanBanHangEntity tkbh = tkbhService.findByUser(currentUser.getUser());


        model.addAttribute("tkbh", tkbh);

        return "shop/vendor-vitien";
    }

    @GetMapping("/vendor/vitien/ruttien")
    public String rutTienTKBH(Model model, @AuthenticationPrincipal CustomUserDetails currentUser ) {
        TaiKhoanBanHangEntity tkbh = tkbhService.findByUser(currentUser.getUser());
        tkbh.setWallet(0);
        tkbhService.updateTkbh(tkbh);


        return "redirect:/vendor/vitien";
    }


//    @GetMapping("/vendor/thongke")
//    public String tkbhThongKe(Model model, @AuthenticationPrincipal CustomUserDetails currentUser ) {
//        TaiKhoanBanHangEntity vendorAccount = tkbhService.findByUser(currentUser.getUser());
//        List<OrderItemEntity> orderItems = orderService.getOrderItemsByVendorId(vendorAccount.getMatkbh());
//
//        model.addAttribute("orderItems", orderItems);
//
//
//        return "shop/vendor-thongke";
//
//    }
@GetMapping("/vendor/thongke")
public String tkbhThongKe(Model model, @AuthenticationPrincipal CustomUserDetails currentUser ) {
    TaiKhoanBanHangEntity vendorAccount = tkbhService.findByUser(currentUser.getUser());
    List<OrderItemEntity> orderItems = orderService.getOrderItemsByVendorId(vendorAccount.getMatkbh());

    // Tổng doanh thu theo tháng
    Map<String, BigDecimal> revenueByMonth = new TreeMap<>();
    Map<String, BigDecimal> revenueByYear = new TreeMap<>();
    Map<String, BigDecimal> revenueByQuarter = new TreeMap<>();

    for (OrderItemEntity item : orderItems) {
        LocalDateTime createdAt = item.getCreatedAt();
        BigDecimal total = BigDecimal.valueOf(item.getPrice())
                .multiply(BigDecimal.valueOf(item.getQuantity()));
        // hoặc item.getPrice().multiply(item.getQuantity())

        String monthKey = createdAt.getMonth().toString(); // JANUARY, FEBRUARY...
        String yearKey = String.valueOf(createdAt.getYear());
        int quarter = (createdAt.getMonthValue() - 1) / 3 + 1;
        String quarterKey = "Q" + quarter + "-" + createdAt.getYear();

        revenueByMonth.merge(monthKey, total, BigDecimal::add);
        revenueByYear.merge(yearKey, total, BigDecimal::add);
        revenueByQuarter.merge(quarterKey, total, BigDecimal::add);
    }

    model.addAttribute("orderItems", orderItems);
    model.addAttribute("revenueByMonth", revenueByMonth);
    model.addAttribute("revenueByYear", revenueByYear);
    model.addAttribute("revenueByQuarter", revenueByQuarter);

    return "shop/vendor-thongke";
}

}