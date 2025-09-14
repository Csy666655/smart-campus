package com.example.userservice.domain.po;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class User {
    private int id;
    private String userName;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private String role;
    private int status;
    private String gender;
    private String avatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
