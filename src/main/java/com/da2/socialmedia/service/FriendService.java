package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.FriendEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.FriendRepository;
import com.da2.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;


    public boolean sendFriendRequest(User sender, Long friendId) {
        User receiver = userRepository.findById(friendId).orElse(null);
        if (receiver == null) {
            return false;
        }

        // Kiểm tra cả hai chiều
        Optional<FriendEntity> existingRequest = friendRepository.findByUser1AndUser2OrUser2AndUser1(sender, receiver, receiver, sender);
        if (existingRequest.isPresent()) {
            return false; // Đã có lời mời kết bạn theo 1 trong 2 chiều
        }

        // Tạo lời mời kết bạn
        FriendEntity friendRequest = new FriendEntity(sender, receiver, "Pending");
        friendRepository.save(friendRequest);
        return true;
    }


    // Chấp nhận lời mời kết bạn
    public boolean acceptFriendRequest(Long requestId) {
        Optional<FriendEntity> requestOpt = friendRepository.findById(requestId);
        if (requestOpt.isPresent()) {
            FriendEntity request = requestOpt.get();
            request.setStatus("Accepted");
            friendRepository.save(request);
            return true;
        }
        return false;
    }

    // Từ chối lời mời kết bạn
    public boolean rejectFriendRequest(Long requestId) {
        Optional<FriendEntity> requestOpt = friendRepository.findById(requestId);
        if (requestOpt.isPresent()) {
            friendRepository.delete(requestOpt.get()); // Xóa lời mời nếu từ chối
            return true;
        }
        return false;
    }

    // Lấy danh sách lời mời kết bạn đang chờ
    public List<FriendEntity> getPendingRequests(User user) {
        return friendRepository.findByUser2AndStatus(user, "Pending");
    }

    public List<User> getFriends(User user) {
        List<FriendEntity> friendEntities = friendRepository.findByUser1AndStatusOrUser2AndStatus(user, "Accepted", user, "Accepted");
        List<User> friends = new ArrayList<>();
        for (FriendEntity friendEntity : friendEntities) {
            if (friendEntity.getUser1().equals(user)) {
                friends.add(friendEntity.getUser2()); // Lấy bạn là user2
            } else {
                friends.add(friendEntity.getUser1()); // Lấy bạn là user1
            }
        }
        return friends;
    }
}
