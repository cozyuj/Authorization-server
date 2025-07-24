package com.forma.Authorization_server.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
}
