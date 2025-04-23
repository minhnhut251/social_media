package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    public User findByEmail(String email);


    @Query("SELECT CASE WHEN f.user1.id = :userId THEN f.user2.id ELSE f.user1.id END " +
            "FROM FriendEntity f WHERE f.user1.id = :userId OR f.user2.id = :userId")
    List<Integer> findFriendIdsByUserId(@Param("userId") int userId);

//    @Query("SELECT u FROM User u WHERE u.id NOT IN :friendIds AND u.id != :userId")
//    List<User> findUsersNotInFriendList(@Param("userId") int userId, @Param("friendIds") List<Integer> friendIds);

    @Query("SELECT u FROM User u WHERE u.id NOT IN :friendIds AND u.id != :userId")
    List<User> findUsersNotInFriendList(@Param("userId") Long userId, @Param("friendIds") List<Long> friendIds);


}


