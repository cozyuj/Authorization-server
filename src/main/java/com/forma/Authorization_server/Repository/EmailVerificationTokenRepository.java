package com.forma.Authorization_server.Repository;

import com.forma.Authorization_server.Domain.EmailVerificationToken;
import com.forma.Authorization_server.Domain.SmsVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<SmsVerificationCode, Long> {
    Optional<EmailVerificationToken> findByEmailAndToken(String email, String token);

    Optional<Object> findByToken(String token);
}
