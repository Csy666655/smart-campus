package com.example.userservice.controller;

import com.example.userservice.domain.dto.LoginDto;
import com.example.userservice.domain.dto.RegisterDto;
import com.example.userservice.domain.po.User;
import com.example.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import utils.Result;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping()
    public User userInfo(){
        return userService.getUserById();
    }
    @PostMapping("/login")
    public Result Login(@RequestBody LoginDto loginDto){
        return userService.login(loginDto);
    }
    @PostMapping("/register")
    public Result Register(@RequestBody RegisterDto registerDto){
        log.info("用户注册：{}",registerDto);
        return userService.regieter(registerDto);
    }
}
