package com.da2.socialmedia.service;

import com.da2.socialmedia.entity.PostEntity;
import com.da2.socialmedia.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;


    public void save(PostEntity post){
        postRepository.save(post);
    }

    public List<PostEntity> getAllPost(){
        return postRepository.findAll();
    }

    public PostEntity getPostById(Long id){
        return postRepository.findById(id).get();
    }

    public void deleteById(Long id){
        postRepository.deleteById(id);
    }
}
