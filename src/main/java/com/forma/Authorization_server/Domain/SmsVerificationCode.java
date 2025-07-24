package com.forma.Authorization_server.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class SmsVerificationCode {
    @Id
    @GeneratedValue
    private Long id;

    private String phoneNumber;
    private String code;
    private boolean verified;
    private LocalDateTime expiresAt;

    @Builder
    public SmsVerificationCode(String phoneNumber, String code, boolean verified, LocalDateTime expiresAt) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.verified = verified;
        this.expiresAt = expiresAt;
    }
}
