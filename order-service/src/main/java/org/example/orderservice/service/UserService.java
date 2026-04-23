package org.example.orderservice.service;

import org.example.orderservice.entity.User;
import org.example.orderservice.exception.ResourceNotFoundException;
import org.example.orderservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAllUsers();
    }

    public  User findUserById(UUID id) {
        Optional<User> user = userRepository.findUserById(id);
        if(user.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        else
            return user.get();
    }
}
