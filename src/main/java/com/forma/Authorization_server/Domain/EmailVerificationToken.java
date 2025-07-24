package com.forma.Authorization_server.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String token;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean verified;

    public static EmailVerificationToken create(String email, String token, Duration validity) {
        EmailVerificationToken evt = new EmailVerificationToken();
        evt.email = email;
        evt.token = token;
        evt.createdAt = LocalDateTime.now();
        evt.expiresAt = evt.createdAt.plus(validity);
        evt.verified = false;
        return evt;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiresAt);
    }
}
