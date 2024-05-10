package com.nikhil.redisdemo.controller;

import com.nikhil.redisdemo.entity.User;
import com.nikhil.redisdemo.service.UserService;
import com.nikhil.redisdemo.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("userController")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/user")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return ResponseEntity.ok("user created successfully");
    }

    @GetMapping(value = "/user")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users=userService.fetchAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") String id){
        User user=userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/user/email/{email}")
    public ResponseEntity<User> getByEmail(@PathVariable(value = "email") String email){
        UserValidator.validate(email);
        return ResponseEntity.ok(userService.getByEmail(email));
    }

}
