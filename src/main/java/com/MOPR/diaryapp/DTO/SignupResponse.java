package com.MOPR.diaryapp.DTO;

import lombok.Data;

@Data
public class SignupResponse {
    private Long id;
    private String username;
    private String email;

    public SignupResponse(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }
}
