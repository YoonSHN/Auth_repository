package com.example.insta_project;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/email")
@RequiredArgsConstructor
public class EmailAuthController {
    private final EmailAuthService authService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestParam String email) {
        // TODO: email 형식 검증(정규식)
        authService.sendVerificationCode(email.trim());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(
            @RequestParam String email,
            @RequestParam String code) {
        boolean ok = authService.verifyCode(email.trim(), code.trim());
        if (ok) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패");
        }
    }
}