package com.nikhil.redisdemo.service;

import com.nikhil.redisdemo.entity.User;
import com.nikhil.redisdemo.manager.RedisCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private RedisCacheManager redisCacheManager;

    public void saveUser(User user) {
        redisCacheManager.saveUser(user);
    }

    public List<User> fetchAllUsers() {
        return redisCacheManager.fetchAllUsers();
    }

    public User getUserById(String id) {
        return redisCacheManager.getUserById(id);
    }

    public User getByEmail(String email) {
        return redisCacheManager.getbyEmail(email);
    }
}
