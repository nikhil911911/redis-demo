package com.nikhil.redisdemo.manager;

import com.nikhil.redisdemo.client.RedisClient;
import com.nikhil.redisdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisCacheManager {
    @Autowired
    private RedisClient redisClient;

    public interface RedisKey{

    }
    public void saveUser(User user) {
        redisClient.save(user);
    }
    public List<User> fetchAllUsers() {
        return redisClient.fetchAllUsers();
    }

    public User getUserById(String id) {
        return redisClient.getUserById(id);
    }

    public User getbyEmail(String email) {
        return redisClient.getByEmail(email);
    }
}
