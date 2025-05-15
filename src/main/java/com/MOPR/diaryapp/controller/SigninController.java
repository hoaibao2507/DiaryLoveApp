package com.MOPR.diaryapp.controller;

import com.MOPR.diaryapp.DTO.LoginResponse;
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
            return ResponseEntity.status(401).body("{\"message\": \"Email hoặc mật khẩu không đúng\"}");
        }

        LoginResponse response = new LoginResponse(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(response);
    }
}