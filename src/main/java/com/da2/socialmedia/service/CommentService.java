package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.CommentEntity;
import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentEntity saveComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    public List<CommentEntity> getCommentsForPost(PostEntity post) {
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }

    public CommentEntity getCommentById(int commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public void deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
    }

    public long countCommentsForPost(PostEntity post) {
        return commentRepository.countByPost(post);
    }
}
