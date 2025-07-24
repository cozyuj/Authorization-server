package com.forma.Authorization_server.Service;

import com.forma.Authorization_server.Domain.SmsVerificationCode;
import com.forma.Authorization_server.Repository.SmsVerificationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SmsVerificationService {
    private final SmsService smsService;
    private final SmsVerificationCodeRepository codeRepository;

    public SmsVerificationService(SmsService smsService,
                                  SmsVerificationCodeRepository codeRepository) {
        this.smsService = smsService;
        this.codeRepository = codeRepository;
    }

    public void sendSmsCode(String phoneNumber) {
        String code = String.valueOf((int)(Math.random() * 900000) + 100000);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);

        SmsVerificationCode entity = SmsVerificationCode.builder()
                .phoneNumber(phoneNumber)
                .code(code)
                .verified(false)
                .expiresAt(expiresAt)
                .build();

        codeRepository.save(entity);
        smsService.sendVerificationCode(phoneNumber, code);
    }

    public boolean confirmSmsCode(String phoneNumber, String code) {
        SmsVerificationCode latest = codeRepository.findTopByPhoneNumberOrderByIdDesc(phoneNumber);
        if (latest == null) return false;
        if (latest.isVerified()) return false;
        if (!latest.getCode().equals(code)) return false;
        if (latest.getExpiresAt().isBefore(LocalDateTime.now())) return false;

        latest.setVerified(true);
        codeRepository.save(latest);
        return true;
    }
}
