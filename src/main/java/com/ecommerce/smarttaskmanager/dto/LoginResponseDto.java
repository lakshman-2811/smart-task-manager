package com.ecommerce.smarttaskmanager.dto;

public class LoginResponseDto {

    private String message;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}