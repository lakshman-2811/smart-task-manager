package com.ecommerce.smarttaskmanager.service;

import com.ecommerce.smarttaskmanager.dto.LoginRequestDto;
import com.ecommerce.smarttaskmanager.dto.LoginResponseDto;
import com.ecommerce.smarttaskmanager.dto.RegisterRequestDto;
import com.ecommerce.smarttaskmanager.dto.UserResponseDto;

public interface UserService {

    UserResponseDto register(RegisterRequestDto request);

    LoginResponseDto login(LoginRequestDto request);

}