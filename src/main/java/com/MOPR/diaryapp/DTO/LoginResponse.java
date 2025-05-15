package com.MOPR.diaryapp.DTO;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String username;
    private String email;

    public LoginResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

}

