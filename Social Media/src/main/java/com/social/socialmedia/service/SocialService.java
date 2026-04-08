package com.social.socialmedia.service;


import com.social.socialmedia.model.SocialUser;
import com.social.socialmedia.repositories.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialService {
    @Autowired
    SocialUserRepository socialUserRepository;

    public List<SocialUser> getAllUsers() {
        return socialUserRepository.findAll();
    }

    public SocialUser saveUser(SocialUser socialUser) {
        socialUser.setSocialProfile();
        return socialUserRepository.save(socialUser);
    }

    public SocialUser deleteUser(Long userId) {
        SocialUser socialUser = socialUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
          socialUserRepository.delete(socialUser);
        return socialUser;
    }
}
