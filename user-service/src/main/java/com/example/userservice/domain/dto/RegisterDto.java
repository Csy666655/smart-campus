package com.example.userservice.domain.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {
    @Size(min = 3,max = 10,message = "用户名长度必须在3~10位之间")
    private String userName;
    @Size(min = 6, max = 20, message = "密码长度必须在6~20之间")
    private String password;
    @Email(message = "邮箱格式不正确")
    private String email;
    @Pattern(regexp = "^1[3-9]\\d{9}$",message = "手机号格式不正确")
    private String phone;
    private String gender;  //性别
    private String realName; //真实姓名
}
