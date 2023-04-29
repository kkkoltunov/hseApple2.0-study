package com.hseapple.service;

import com.hseapple.dao.ProfileDao;
import com.hseapple.dao.ProfileEntity;
import com.hseapple.dao.UserDao;
import com.hseapple.dao.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Autowired
    ProfileDao profileDao;

    public UserEntity createUser(String commonname, String email) {
        Optional<UserEntity> user = userDao.findByEmail(email);
        System.out.println(user);
        System.out.println(email);
        if (user.isPresent()) {
            return user.get();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setCommonname(commonname);
        userEntity.setEmail(email);
        userEntity.setCreatedAt(LocalDateTime.now());
        userDao.save(userEntity);
        System.out.println(userEntity.getId());
        createProfile(userEntity.getId());
        return userEntity;
    }

    private void createProfile(Long id) {
        ProfileEntity profile = new ProfileEntity();
        profile.setUserID(id);
        profile.setCreatedAt(LocalDateTime.now());
        profileDao.save(profile);
    }
}
