package com.example.userservice.service;

import com.example.userservice.domain.dto.LoginDto;
import com.example.userservice.domain.dto.RegisterDto;
import com.example.userservice.domain.po.User;
import utils.Result;

public interface UserService {
    User getUserById();

    Result login(LoginDto loginDto);

    Result regieter(RegisterDto registerDto);
}
