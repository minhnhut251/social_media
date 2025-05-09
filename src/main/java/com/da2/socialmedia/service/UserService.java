package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.UserRepository;
import com.da2.socialmedia.security.CustomUserDetails;
import jakarta.transaction.Transactional;
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
    private final PostService postService;

    @Autowired
    public UserService(UserRepository userRepository, FileService fileService,PostService postService) {
        this.userRepository = userRepository;
        this.fileService = fileService;
        this.postService=postService;
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

    public void lockUser(Long id) {
        User existingUser = getUserById(id);

        // Update user fields
        existingUser.setLocked(true);

        userRepository.save(existingUser);
    }

    public void unlockUser(Long id) {
        User existingUser = getUserById(id);

        // Update user fields
        existingUser.setLocked(false);

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

    public void updateBanner(Long id, MultipartFile banner_file) {
        System.out.println("Update avatar service");
        User user = getUserById(id);

        if (banner_file != null && !banner_file.isEmpty()) {

            fileService.deleteFileIfExists(user.getBanner());

            String bannerUrl = fileService.handleFileUpload((banner_file));
            System.out.println(bannerUrl);
            System.out.println(user.getId());
            user.setBanner(bannerUrl);
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

//    public void deleteUserById(Long id) {
//        userRepository.deleteById(id);
//    }

    public void deleteUserById(Long id) {
        User user = getUserById(id);

        if (user.getPosts() != null) {
            user.getPosts().clear();  //

        if (user.getTkbh() != null) {
            user.setTkbh(null);
        }

        userRepository.delete(user);
        }
    }
//    @Transactional
//    public void forceDeleteUser(User user) {
//        // Xóa avatar và banner nếu có
//        fileService.deleteFileIfExists(user.getAvatar());
//        fileService.deleteFileIfExists(user.getBanner());
//
//        // Nếu bài đăng có liên kết ngược thì xóa bài đăng (cascade đã có)
//        userRepository.delete(user); // do cascade = ALL và orphanRemoval = true
//    }
@Transactional
public void forceDeleteUser(User user) {
    // Kiểm tra và xóa avatar nếu tồn tại
    if (user.getAvatar() != null) {
        fileService.deleteFileIfExists(user.getAvatar());
    }

    // Kiểm tra và xóa banner nếu tồn tại
    if (user.getBanner() != null) {
        fileService.deleteFileIfExists(user.getBanner());
    }

    // Nếu bài đăng có liên kết ngược thì xóa bài đăng trước (nếu cascade không làm việc như mong muốn)
    if (user.getPosts() != null && !user.getPosts().isEmpty()) {
        for (PostEntity postEntity : user.getPosts()) {
            postService.deletePost(postEntity); // Giả sử bạn có phương thức xóa bài viết trong service
        }
    }

    // Xóa user (cascade sẽ tự xử lý xóa các bài đăng liên quan)
    userRepository.delete(user);
}


}