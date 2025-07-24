package com.forma.Authorization_server.Repository;

import com.forma.Authorization_server.Domain.SmsVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsVerificationCodeRepository extends JpaRepository<SmsVerificationCode, Long> {
    SmsVerificationCode findTopByPhoneNumberOrderByIdDesc(String phoneNumber);
}
