package com.ecommerce.smarttaskmanager.controller;

import com.ecommerce.smarttaskmanager.dto.LoginRequestDto;
import com.ecommerce.smarttaskmanager.dto.LoginResponseDto;
import com.ecommerce.smarttaskmanager.dto.RegisterRequestDto;
import com.ecommerce.smarttaskmanager.dto.UserResponseDto;
import com.ecommerce.smarttaskmanager.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody RegisterRequestDto request) {

        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponseDto login(
            @RequestBody LoginRequestDto request){

        return userService.login(request);

    }
}