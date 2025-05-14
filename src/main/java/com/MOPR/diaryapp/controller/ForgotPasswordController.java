package com.MOPR.diaryapp.controller;

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
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private EmailService emailService;

    // API hiển thị form quên mật khẩu (có thể bỏ qua ở phía client mobile)
    @GetMapping
    public ResponseEntity<Map<String, String>> showForgotPasswordForm() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Form quên mật khẩu.");
        return ResponseEntity.ok(response);
    }

    // API xử lý quên mật khẩu, gửi OTP qua email
    @PostMapping
    public ResponseEntity<Map<String, Object>> processForgotPassword(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            User user = userService.findByEmail(email);

            // Tạo và gửi OTP
            String otp = otpService.generateOTP(email);
            emailService.sendPasswordResetEmail(email, otp);

            response.put("success", true);
            response.put("message", "Mã OTP đã được gửi tới email của bạn.");
            response.put("email", email);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Email không tồn tại trong hệ thống.");
        }

        return ResponseEntity.ok(response);
    }

    // API xác minh OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOTP(@RequestParam String email,
                                                         @RequestParam String otp) {
        Map<String, Object> response = new HashMap<>();
        if (otpService.validateOTP(email, otp)) {
            response.put("success", true);
            response.put("message", "Mã OTP hợp lệ.");
        } else {
            response.put("success", false);
            response.put("message", "Mã OTP không hợp lệ.");
        }
        return ResponseEntity.ok(response);
    }

    // API để reset mật khẩu sau khi xác minh OTP thành công
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam String email,
                                                             @RequestParam String newPassword) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.findByEmail(email);
            userService.updatePassword(user, newPassword);
            otpService.clearOTP(email); // Xóa OTP sau khi đổi mật khẩu thành công

            response.put("success", true);
            response.put("message", "Đổi mật khẩu thành công.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Đổi mật khẩu thất bại.");
        }

        return ResponseEntity.ok(response);
    }
}