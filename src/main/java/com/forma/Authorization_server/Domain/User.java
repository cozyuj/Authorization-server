package com.forma.Authorization_server.Domain;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    private String phoneNumber;
    private boolean phoneVerified;

    private String email;
    private boolean emailVerified;

    @Builder
    public User(String username, String password, String phoneNumber, boolean phoneVerified, String email, boolean emailVerified) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.phoneVerified = phoneVerified;
        this.email = email;
        this.emailVerified = emailVerified;
    }


}
