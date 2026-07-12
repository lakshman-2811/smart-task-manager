package com.ecommerce.smarttaskmanager.service.impl;

import com.ecommerce.smarttaskmanager.dto.LoginRequestDto;
import com.ecommerce.smarttaskmanager.dto.LoginResponseDto;
import com.ecommerce.smarttaskmanager.dto.RegisterRequestDto;
import com.ecommerce.smarttaskmanager.dto.UserResponseDto;
import com.ecommerce.smarttaskmanager.entity.User;
import com.ecommerce.smarttaskmanager.enums.UserRole;
import com.ecommerce.smarttaskmanager.repository.UserRepository;
import com.ecommerce.smarttaskmanager.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDto register(RegisterRequestDto request) {

        log.info("Registering user with email: {}", request.getEmail());

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            log.warn("User already exists with email: {}", request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        log.info("User registered successfully with id: {}", savedUser.getId());

        UserResponseDto response = new UserResponseDto();

        response.setId(savedUser.getId());
        response.setName(savedUser.getName());
        response.setEmail(savedUser.getEmail());
        response.setRole(savedUser.getRole());
        response.setCreatedAt(savedUser.getCreatedAt());

        return response;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {

        log.info("Login attempt for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            log.warn("Invalid password for {}", request.getEmail());

            throw new RuntimeException("Invalid Password");
        }

        log.info("User logged in successfully: {}", request.getEmail());

        return new LoginResponseDto("Login Successful");
    }
}