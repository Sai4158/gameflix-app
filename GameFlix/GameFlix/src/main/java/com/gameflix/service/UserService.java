package com.gameflix.service;

import com.gameflix.model.User;
import com.gameflix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        // First, check if the username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // MAKE SURE THIS METHOD TAKES TWO STRINGS
    public boolean loginUser(String username, String rawPassword) {
        // Find the user by their username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        // Check if the provided password matches the hashed password in the database
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}