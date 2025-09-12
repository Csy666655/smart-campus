package com.example.userservice.service.impl;

import com.example.userservice.domain.po.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User getUserById() {
        return userMapper.getUserById();
    }
}
