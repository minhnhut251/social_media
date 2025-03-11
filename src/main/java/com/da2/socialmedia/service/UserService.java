package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FileService fileService;

    @Autowired
    public UserService(UserRepository userRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserProfile(User user) {
        User existingUser = getUserById(user.getId());

        // Update user fields
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setMoTa(user.getMoTa());
        existingUser.setNgaysinh(user.getNgaysinh());
        existingUser.setSex(user.getSex());
        existingUser.setDiaChi(user.getDiaChi());
        existingUser.setHocVan(user.getHocVan());
        existingUser.setNoiLamViec(user.getNoiLamViec());
        existingUser.setSdt(user.getSdt());

        userRepository.save(existingUser);
    }


//    public void updateUserAvatar(Long userId, MultipartFile file) {
//        if (file != null && !file.isEmpty()) {
//            User user = getUserById(userId);
//
//            // Delete old avatar if exists
//            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
//                fileService.deleteFileIfExists(user.getAvatar());
//            }
//
//            // Upload new avatar
//            String avatarUrl = fileService.handleFileUpload(file);
//            user.setAvatar(avatarUrl);
//
//            userRepository.save(user);
//        }
//    }
//
//    public void updateUserBanner(Long userId, MultipartFile file) {
//        if (file != null && !file.isEmpty()) {
//            User user = getUserById(userId);
//
//            // Delete old banner if exists
//            if (user.getBanner() != null && !user.getBanner().isEmpty()) {
//                fileService.deleteFileIfExists(user.getBanner());
//            }
//
//            // Upload new banner
//            String bannerUrl = fileService.handleFileUpload(file);
//            user.setBanner(bannerUrl);
//
//            userRepository.save(user);
//        }
//    }

}