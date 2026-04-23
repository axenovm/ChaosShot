package org.example.orderservice.controller;

import org.example.orderservice.entity.User;
import org.example.orderservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable UUID id){
        return userService.findUserById(id);
    }

    @GetMapping
    public List<User> findAllUser(){
        return userService.findAllUsers();
    }
}
