package com.forma.Authorization_server.Service;

import com.forma.Authorization_server.Domain.EmailVerificationToken;
import com.forma.Authorization_server.Domain.User;
import com.forma.Authorization_server.Repository.EmailVerificationTokenRepository;
import com.forma.Authorization_server.Repository.UserRepository;
import com.forma.Authorization_server.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtProvider;

    private final JavaMailSender mailSender;

    public String createVerificationToken(String email) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setEmail(email);
        verificationToken.setToken(token);
        verificationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        verificationToken.setVerified(false);
        tokenRepository.save(verificationToken);

        // 이메일 전송 생략
        return token;
    }
    public TokenResponse verifyEmailTokenAndReissueJWT(String token) {
        EmailVerificationToken verificationToken = (EmailVerificationToken) tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 토큰"));

        if (verificationToken.isVerified()) {
            throw new RuntimeException("이미 인증됨");
        }

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("토큰 만료");
        }

        verificationToken.setVerified(true);
        tokenRepository.save(verificationToken);

        User user = (User) userRepository.findByEmail(verificationToken.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        // 이메일 인증 처리
        user.setEmailVerified(true);
        userRepository.save(user);

        //최종 JWT 재발급
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        return new TokenResponse(accessToken, refreshToken);
    }

    public void sendVerificationEmail(String email) {
        String token = UUID.randomUUID().toString().substring(0, 6); // 6자리 코드
        EmailVerificationToken verificationToken = EmailVerificationToken.create(email, token, Duration.ofMinutes(10));
        tokenRepository.save(verificationToken);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 코드");
        message.setText("인증 코드: " + token);
        mailSender.send(message);
    }

    public boolean verifyEmail(String email, String token) {
        Optional<EmailVerificationToken> found = tokenRepository.findByEmailAndToken(email, token);

        if (found.isEmpty() || found.get().isExpired()) return false;

        EmailVerificationToken evt = found.get();
        evt.setVerified(true);
        tokenRepository.save(evt);
        return true;
    }
}
