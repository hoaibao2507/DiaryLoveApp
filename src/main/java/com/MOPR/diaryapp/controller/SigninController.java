package com.MOPR.diaryapp.controller;

import com.MOPR.diaryapp.model.User;
import com.MOPR.diaryapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class SigninController {

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestParam String email, @RequestParam String password) {
        User user = userService.authenticate(email, password);

        if (user == null) {
            return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng");
        }

        // Bạn có thể trả thêm thông tin người dùng nếu cần
        return ResponseEntity.ok("Đăng nhập thành công: " + user.getUsername());
    }
}