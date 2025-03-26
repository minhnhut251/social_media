package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.MessageEntity;
import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    // Tim tin nhan trong cuoc tro truyen

    @Query("SELECT m FROM MessageEntity m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.createdAt ASC")
    List<MessageEntity> findMessagesBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);
}
