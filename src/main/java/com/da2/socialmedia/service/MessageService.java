package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.MessageEntity;
import com.da2.socialmedia.entity.User;
import com.da2.socialmedia.repository.MessageRepository;
import com.da2.socialmedia.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void createMessage(MessageEntity message, User nguoiGui, User nguoiNhan){
        message.setSender(nguoiGui);
        message.setReceiver(nguoiNhan);
        messageRepository.save(message);
    }

    public List<MessageEntity> getConversation(User nguoiGui, User nguoiNhan){
        return messageRepository.findMessagesBetweenUsers(nguoiGui, nguoiNhan);
    }
}
