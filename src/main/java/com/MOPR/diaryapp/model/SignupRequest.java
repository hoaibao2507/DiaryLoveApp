package com.MOPR.diaryapp.model;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
}
