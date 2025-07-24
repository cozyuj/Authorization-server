package com.forma.Authorization_server.Controller;

import com.forma.Authorization_server.Service.EmailVerificationService;
import com.forma.Authorization_server.Service.SmsVerificationService;
import com.forma.Authorization_server.dto.SmsSendRequest;
import com.forma.Authorization_server.dto.SmsVerifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/verify")
public class VerificationController {
    @Autowired
    private SmsVerificationService smsVerificationService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/email/send")
    public ResponseEntity<?> sendEmail(@RequestParam String email) {
        emailVerificationService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증 이메일 전송됨");
    }

    @PostMapping("/email/confirm")
    public ResponseEntity<?> verifyEmail(@RequestParam String email, @RequestParam String token) {
        boolean result = emailVerificationService.verifyEmail(email, token);
        if (result) return ResponseEntity.ok("인증 완료");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
    }

    @PostMapping("/sms/send")
    public ResponseEntity<?> sendSms(@RequestBody SmsSendRequest request) {
        smsVerificationService.sendSmsCode(request.getPhoneNumber());
        return ResponseEntity.ok("전송됨");
    }

    @PostMapping("/sms/confirm")
    public ResponseEntity<?> confirmSms(@RequestBody SmsVerifyRequest request) {
        boolean success = smsVerificationService.confirmSmsCode(request.getPhoneNumber(), request.getCode());
        return success ? ResponseEntity.ok("인증 성공") : ResponseEntity.badRequest().body("실패");
    }
}
