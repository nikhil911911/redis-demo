package com.nikhil.redisdemo.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikhil.redisdemo.entity.User;
import com.nikhil.redisdemo.properties.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class RedisClient {
    private final JedisPool jedisPool;

    @Autowired
    public RedisClient(RedisProperties redisProperties) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(128);
        jedisPoolConfig.setMaxIdle(128);
        jedisPoolConfig.setMinIdle(16);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
//        jedisPoolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
//        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
//        jedisPoolConfig.setNumTestsPerEvictionRun(3);
//        jedisPoolConfig.setBlockWhenExhausted(true);
        String address = String.format("%s", redisProperties.getHost());
        this.jedisPool = new JedisPool(jedisPoolConfig, address);
    }

    public void save(User user) {
        try (Jedis jedis = jedisPool.getResource()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(user);
            jedis.set("userId" + user.getId(), jsonString);
            jedis.sadd("userKeys", "userId" + user.getId());
            jedis.set("userEmail" + user.getEmailId(), "userId" + user.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> fetchAllUsers() {
        try (Jedis jedis = jedisPool.getResource()) {
            List<User> users = new ArrayList<>();
            Set<String> userKeys = jedis.smembers("userKeys");
            ObjectMapper objectMapper = new ObjectMapper();
            for (String userKey : userKeys) {
                String jsonString = jedis.get(userKey);
                User user = objectMapper.readValue(jsonString, User.class);
                users.add(user);
            }
            return users;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserById(String id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String jsonString = jedis.get("userId" + id);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public User getByEmail(String email) {
        try (Jedis jedis = jedisPool.getResource()) {
            String userId = jedis.get("userEmail" + email);
            userId=userId.replace("userId","");
            String jsonString = jedis.get("userId" + userId);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
