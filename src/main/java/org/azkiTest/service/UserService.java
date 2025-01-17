package org.azkiTest.service;

import org.azkiTest.DTOs.LoginRequest;
import org.azkiTest.DTOs.RegisterRequest;
import org.azkiTest.config.JwtTokenUtil;
import org.azkiTest.model.Users;
import org.azkiTest.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserService(UsersRepository userRepository, JwtTokenUtil jwtTokenUtil , PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists.");
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    public String authUser(LoginRequest request) {
            Users user = (Users) userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Invalid credentials"));

            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return jwtTokenUtil.generateToken(user.getUsername());
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        }
    }



