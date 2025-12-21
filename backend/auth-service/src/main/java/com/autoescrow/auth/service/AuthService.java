package com.autoescrow.auth.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.autoescrow.auth.dto.LoginRequest;
import com.autoescrow.auth.dto.RegisterRequest;
import com.autoescrow.auth.entity.User;
import com.autoescrow.auth.repository.UserRepository;
import com.autoescrow.auth.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ✅ REGISTER
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setStatus(true);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    // ✅ LOGIN
    public String login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // ✅ STATIC CALL (CORRECT FOR YOUR JwtUtil)
        return JwtUtil.generateToken(user.getEmail());
    }
}
