package com.MOPR.diaryapp.controller;

import com.MOPR.diaryapp.DTO.SignupResponse;
import com.MOPR.diaryapp.model.SignupRequest;
import com.MOPR.diaryapp.model.User;
import com.MOPR.diaryapp.service.EmailService;
import com.MOPR.diaryapp.service.OTPService;
import com.MOPR.diaryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/signup")
public class SignupController {

    @Autowired private UserService userService;
    @Autowired private OTPService otpService;
    @Autowired private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> processSignup(@RequestBody SignupRequest signupRequest) {

        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Mật khẩu không trùng khớp"
            ));
        }

        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email đã được sử dụng"
            ));
        }

        String otp = otpService.generateOTP(signupRequest.getEmail());
        emailService.sendOTPEmail(signupRequest.getEmail(), otp);

        // ✅ Trả về chính đối tượng SignupRequest
        return ResponseEntity.ok(signupRequest);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String username,
            @RequestParam String password) {

        if (!otpService.validateOTP(email, otp)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Mã OTP không đúng"
            ));
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        userService.saveUser(user);
        otpService.clearOTP(email);

        SignupResponse response = new SignupResponse(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOTP(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            String otp = otpService.generateOTP(email);
            emailService.sendOTPEmail(email, otp);
            response.put("success", true);
            response.put("message", "Đã gửi lại mã OTP");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi gửi OTP: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }
}