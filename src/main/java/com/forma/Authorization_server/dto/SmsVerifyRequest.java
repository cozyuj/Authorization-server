package com.forma.Authorization_server.dto;

import lombok.Data;

@Data
public class SmsVerifyRequest {
    private String phoneNumber;
    private String code;
}
