package com.MOPR.diaryapp.controller;

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
        Map<String, Object> response = new HashMap<>();

        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            response.put("success", false);
            response.put("message", "Mật khẩu không trùng khớp");
            return ResponseEntity.badRequest().body(response);
        }

        if (userService.existsByEmail(signupRequest.getEmail())) {
            response.put("success", false);
            response.put("message", "Email đã được sử dụng");
            return ResponseEntity.badRequest().body(response);
        }

        String otp = otpService.generateOTP(signupRequest.getEmail());
        emailService.sendOTPEmail(signupRequest.getEmail(), otp);

        response.put("success", true);
        response.put("message", "Đã gửi mã OTP tới email");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String username,
            @RequestParam String password) {

        Map<String, Object> response = new HashMap<>();

        if (!otpService.validateOTP(email, otp)) {
            response.put("success", false);
            response.put("message", "Mã OTP không đúng");
            return ResponseEntity.badRequest().body(response);
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        userService.saveUser(user);
        otpService.clearOTP(email);

        response.put("success", true);
        response.put("message", "Đăng ký thành công");
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