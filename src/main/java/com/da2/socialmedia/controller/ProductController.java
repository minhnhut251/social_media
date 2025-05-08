package com.da2.socialmedia.controller;

import com.da2.socialmedia.entity.SanphamEntity;
import com.da2.socialmedia.entity.TaiKhoanBanHangEntity;
import com.da2.socialmedia.security.CustomUserDetails;
import com.da2.socialmedia.service.ProductService;
import com.da2.socialmedia.service.TKBHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Controller
@RequestMapping("/vendor")
public class ProductController {

    private final ProductService productService;
    private final TKBHService tkbhService;

    @Autowired
    public ProductController(ProductService productService, TKBHService tkbhService) {
        this.productService = productService;
        this.tkbhService = tkbhService;
    }

    @GetMapping("")
    public String vendorDashboard(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        // Get vendor information
        TaiKhoanBanHangEntity vendorInfo = tkbhService.findByUser(currentUser.getUser());
        model.addAttribute("vendorInfo", vendorInfo);

        return "shop/vendor";
    }

    @GetMapping("/products")
    public String vendorProducts(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        List<SanphamEntity> products = productService.getProductsByTkbhMatkbh(tkbhService.findByUser(currentUser.getUser()).getMatkbh());
        model.addAttribute("products", products);
        model.addAttribute("product", new SanphamEntity()); // For the add product form

        // Get vendor information
        TaiKhoanBanHangEntity vendorInfo = tkbhService.findByUser(currentUser.getUser());
        model.addAttribute("vendorInfo", vendorInfo);

        return "shop/vendor-products";
    }

    @PostMapping("/product/add")
    public String addProduct(SanphamEntity product,
                             @RequestParam(value = "productImage", required = false) MultipartFile file,
                             @AuthenticationPrincipal CustomUserDetails currentUser,
                             RedirectAttributes redirectAttributes) {
        try {
            TaiKhoanBanHangEntity vendorInfo = tkbhService.findByUser(currentUser.getUser());

            // Set timestamps
            Timestamp now = Timestamp.from(Instant.now());
            product.setCreatedAt(now);
            product.setUpdatedAt(now);

            // Save the product
            productService.createProduct(product, vendorInfo, file);

            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được thêm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/vendor/products";
    }

    @GetMapping("/product/{id}")
    public String showProductEditForm(@PathVariable("id") Long id, Model model) {
        SanphamEntity product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "shop/product_edit";
    }

    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                SanphamEntity product,
                                @RequestParam(value = "productImage", required = false) MultipartFile file,
                                RedirectAttributes redirectAttributes) {
        try {
            // Update timestamp
            product.setUpdatedAt(Timestamp.from(Instant.now()));

            // Update product
            productService.updateProduct(id, product, file);

            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/vendor/products";
    }

    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/vendor/products";
    }

    @GetMapping("/product/discontinue/{id}")
    public String discontinueProduct(@PathVariable("id") Long id,
                                RedirectAttributes redirectAttributes) {
        try {
            productService.discontinueProduct(id);
            redirectAttributes.addFlashAttribute("success", "Đã ngừng kinh doanh sản phẩm!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/vendor/products";
    }


    @GetMapping("/shop")
    public String showShopPage(Model model) {
        List<SanphamEntity> products = productService.getAllProducts();
        model.addAttribute("products", products);
        System.out.println("Number of products: " + products.size());
        return "shop/shop";
    }
}