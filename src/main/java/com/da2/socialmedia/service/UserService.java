package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.UserRepository;
import com.da2.socialmedia.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    public List<User> getAllUsers() {
        return userRepository.findAll();
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



    public void updateAvatar(Long id, MultipartFile avatar_file) {
        System.out.println("Update avatar service");
        User user = getUserById(id);

        if (avatar_file != null && !avatar_file.isEmpty()) {
            // Delete old avatar if exists
            fileService.deleteFileIfExists(user.getAvatar());

            // Save new avatar
            String avatarUrl = fileService.handleFileUpload((avatar_file));
            System.out.println(avatarUrl);
            System.out.println(user.getId());
            user.setAvatar(avatarUrl);
        }

        userRepository.save(user);

    }

    public void refreshAuthenticationPrincipal(User updatedUser) {
        // Get currently authenticated user
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        // Create new CustomUserDetails with updated user info
        CustomUserDetails updatedPrincipal = new CustomUserDetails(updatedUser);

        // Create new authentication with the updated principal
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedPrincipal,
                currentAuth.getCredentials(),
                currentAuth.getAuthorities());

        // Set the new authentication object
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}