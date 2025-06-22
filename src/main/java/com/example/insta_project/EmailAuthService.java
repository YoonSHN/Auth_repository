package com.example.insta_project;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@RequiredArgsConstructor
@Service
public class EmailAuthService {
    private final EmailService emailService;
    private final StringRedisTemplate redis;

    private static final Duration CODE_TTL = Duration.ofMinutes(3);

    public void sendVerificationCode(String email) {
        String code = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        // Redis에 저장
        redis.opsForValue().set("email:" + email, code, CODE_TTL);
        // 이메일 발송
        String subject = "MyApp 이메일 인증 코드";
        String body    = "인증 코드는 " + code + " 입니다. (유효시간 3분)";
        emailService.sendEmail(email, subject, body);
    }

    public boolean verifyCode(String email, String inputCode) {
        String saved = redis.opsForValue().get("email:" + email);
        return inputCode != null && inputCode.equals(saved);
    }
}