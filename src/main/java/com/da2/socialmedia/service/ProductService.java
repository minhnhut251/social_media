package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.SanphamEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final FileService fileService;

    @Autowired
    public ProductService(ProductRepository productRepository, FileService fileService) {
        this.productRepository = productRepository;
        this.fileService = fileService;
    }

    public List<SanphamEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public List<SanphamEntity> getProductsByUserId(Long userId) {
        return productRepository.findByUsersId(userId);
    }

    public SanphamEntity getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));
    }

    public void createProduct(SanphamEntity product, User user, MultipartFile file) {
        product.setUsers(user);

        if (file != null && !file.isEmpty()) {
            String imageUrl = fileService.handleFileUpload(file);
            product.setImg(imageUrl);
        }

        productRepository.save(product);
    }

    public void updateProduct(int id, SanphamEntity productDetails, MultipartFile file) {
        SanphamEntity existingProduct = getProductById(id);

        // Update fields
        existingProduct.setTensp(productDetails.getTensp());
        existingProduct.setSl(productDetails.getSl());
        existingProduct.setGia(productDetails.getGia());
        existingProduct.setMoTa(productDetails.getMoTa());
        existingProduct.setUpdatedAt(productDetails.getUpdatedAt());

        // Handle image upload if provided
        if (file != null && !file.isEmpty()) {
            // Delete old image
            fileService.deleteFileIfExists(existingProduct.getImg());

            // Upload new image
            String imageUrl = fileService.handleFileUpload(file);
            existingProduct.setImg(imageUrl);
        }

        productRepository.save(existingProduct);
    }

    public void deleteProduct(int id) {
        SanphamEntity product = getProductById(id);

        // Delete associated image
        fileService.deleteFileIfExists(product.getImg());

        productRepository.delete(product);
    }
}