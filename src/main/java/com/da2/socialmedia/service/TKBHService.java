package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.entity.TaiKhoanBanHangEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.TKBHRepository;
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
public class TKBHService {

    private final TKBHRepository tkbhRepository;

    @Autowired
    public TKBHService(TKBHRepository tkbhRepository) {
        this.tkbhRepository = tkbhRepository;
    }

    public void createTkbh(TaiKhoanBanHangEntity taiKhoanBanHang) {
        tkbhRepository.save(taiKhoanBanHang);
    }

    public TaiKhoanBanHangEntity findByUser(User user) {
        // You'll need to update the TKBHRepository to include this method
        return tkbhRepository.findByUser(user);
    }

//    public TaiKhoanBanHangEntity getTkbhByMatk(Long id) {
//        return tkbhRepository.findByUser(id);
//    }

}