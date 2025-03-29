package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.FriendEntity;
import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
    boolean existsByUser1AndUser2(User user1, User user2);
    List<FriendEntity> findByUser2AndStatus(User user, String status);


    List<FriendEntity> findByUser1AndStatusOrUser2AndStatus(User user1, String status1, User user2, String status2);


    Optional<FriendEntity> findByUser1AndUser2(User user1, User user2);

    Optional<FriendEntity> findByUser1AndUser2OrUser2AndUser1(User user1, User user2, User user2Again, User user1Again);


    @Query("SELECT CASE WHEN f.user1.id = :userId THEN f.user2.id ELSE f.user1.id END " +
            "FROM FriendEntity f WHERE (f.user1.id = :userId OR f.user2.id = :userId) AND f.status = 'Accepted'")
    List<Long> findConfirmedFriendIds(@Param("userId") Long userId);

    @Query("SELECT u FROM User u WHERE u.id NOT IN :friendIds AND u.id != :userId")
    List<User> findUsersNotInFriendList(@Param("userId") Long userId, @Param("friendIds") List<Long> friendIds);

}

