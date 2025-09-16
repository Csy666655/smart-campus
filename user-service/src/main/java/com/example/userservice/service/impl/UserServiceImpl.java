package com.example.userservice.service.impl;

import com.example.userservice.domain.dto.LoginDto;
import com.example.userservice.domain.dto.RegisterDto;
import com.example.userservice.domain.po.Student;
import com.example.userservice.domain.po.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.service.UserService;
import com.example.userservice.utils.Constants;
import com.example.userservice.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import com.common.utils.Result;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    //密码加密对象
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public User getUserById() {
        return userMapper.getUserById();
    }

    /**
     * 用户登录
     * @param loginDto
     * @return
     */
    @Override
    public Result login(LoginDto loginDto) {
        String userName = loginDto.getUserName();
        String password = loginDto.getPassword();
        User user = userMapper.getUserByName(userName);
        if(user == null){
            return Result.error("用户不存在!");
        }
        boolean b = passwordEncoder.matches(password,user.getPassword());
        if(b){
            // 生成JWT token
            String token = jwtUtil.generateToken(user.getId(), user.getUserName());
            
            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("userId", user.getId());
            data.put("userName", user.getUserName());
            data.put("email", user.getEmail());
            
            return Result.success(data);
        }else{
            return Result.error("用户名或密码错误!");
        }
    }

    /**
     * 用户注册
     * @param registerDto
     * @return
     */
    @Override
    public Result regieter(RegisterDto registerDto) {
        log.info("用户注册："+registerDto);

        User user = User.builder()
                .userName(registerDto.getUserName())
                .email(registerDto.getEmail())
                .phone(registerDto.getPhone())
                .realName(registerDto.getRealName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(1)
                .gender(registerDto.getGender())
                .build();
        String raw = registerDto.getPassword();
        user.setAvatar("无");
        user.setPassword(passwordEncoder.encode(raw));
        user.setRole(Constants.ROLE_STUDENT);
        userMapper.insert(user);
        return Result.success("注册成功");
    }

    @Override
    public Result bindStudentInfo(Student student) {
        //TODO:绑定学号，获取返回的id，在向user表中添加学号id;
        return null;
    }
}
