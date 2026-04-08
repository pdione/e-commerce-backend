package com.social.socialmedia.repositories;


import com.social.socialmedia.model.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialUserRepository extends JpaRepository<SocialUser,Long> {
}
