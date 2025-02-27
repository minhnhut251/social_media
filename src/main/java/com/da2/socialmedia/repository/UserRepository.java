package com.da2.socialmedia.repository;

import com.da2.socialmedia.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
