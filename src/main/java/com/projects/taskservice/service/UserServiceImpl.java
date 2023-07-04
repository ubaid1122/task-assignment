package com.projects.taskservice.service;

import com.projects.taskservice.entity.User;
import com.projects.taskservice.repository.UserRepository;
import com.projects.taskservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User registerUser(User user) {
        // Set the encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user in the repository
        return userRepository.save(user);
    }
}