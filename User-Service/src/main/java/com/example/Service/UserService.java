package com.example.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        // System Design: Data Partitioning - Shard by user ID
        user.setRole("USER"); // Default role for RBAC
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        // System Design: Fault Tolerance - Graceful error handling
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}