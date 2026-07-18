package com.ecommerce.smarttaskmanager.service.impl;

import com.ecommerce.smarttaskmanager.dto.LoginRequestDto;
import com.ecommerce.smarttaskmanager.dto.LoginResponseDto;
import com.ecommerce.smarttaskmanager.dto.RegisterRequestDto;
import com.ecommerce.smarttaskmanager.dto.UserResponseDto;
import com.ecommerce.smarttaskmanager.entity.User;
import com.ecommerce.smarttaskmanager.enums.UserRole;
import com.ecommerce.smarttaskmanager.repository.UserRepository;
import com.ecommerce.smarttaskmanager.security.JwtService;
import com.ecommerce.smarttaskmanager.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private RegisterRequestDto registerRequest;
    private LoginRequestDto loginRequest;

    @BeforeEach
    void setUp() {

        user = new User();

        user.setId(1L);
        user.setName("Lakshman");
        user.setEmail("lakshman@gmail.com");
        user.setPassword("encodedPassword");
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());

        registerRequest = new RegisterRequestDto();

        registerRequest.setName("Lakshman");
        registerRequest.setEmail("lakshman@gmail.com");
        registerRequest.setPassword("123456");

        loginRequest = new LoginRequestDto();

        loginRequest.setEmail("lakshman@gmail.com");
        loginRequest.setPassword("123456");
    }

    @Test
    void shouldRegisterUserSuccessfully() {

        when(userRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponseDto response =
                userService.register(registerRequest);

        assertNotNull(response);
        assertEquals("Lakshman", response.getName());
        assertEquals("lakshman@gmail.com", response.getEmail());

        verify(userRepository)
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(userRepository.findByEmail(registerRequest.getEmail()))
                .thenReturn(Optional.of(user));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> userService.register(registerRequest));

        assertEquals(
                "Email already exists",
                exception.getMessage());

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void shouldLoginSuccessfully() {

        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()))
                .thenReturn(true);

        when(jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()))
                .thenReturn("jwt-token");

        LoginResponseDto response =
                userService.login(loginRequest);

        assertNotNull(response);
        assertEquals(
                "jwt-token",
                response.getToken());

        verify(jwtService)
                .generateToken(
                        user.getEmail(),
                        user.getRole().name());
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {

        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> userService.login(loginRequest));

        assertEquals(
                "Invalid Email",
                exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {

        when(userRepository.findByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()))
                .thenReturn(false);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> userService.login(loginRequest));

        assertEquals(
                "Invalid Password",
                exception.getMessage());
    }
}