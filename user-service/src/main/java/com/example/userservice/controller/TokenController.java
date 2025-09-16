package com.example.userservice.controller;

import com.example.userservice.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.common.utils.Result;

import java.util.HashMap;
import java.util.Map;

/**
 * Token相关控制器
 * 用于测试和验证JWT token的时间功能
 */
@RestController
@Slf4j
@RequestMapping("/token")
public class TokenController {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 验证token有效性
     * @param token JWT token
     * @return 验证结果
     */
    @PostMapping("/validate")
    public Result validateToken(@RequestParam String token) {
        try {
            boolean isValid = jwtUtil.validateToken(token);
            Map<String, Object> result = new HashMap<>();
            result.put("isValid", isValid);
            
            if (isValid) {
                result.put("userId", jwtUtil.getUserIdFromToken(token));
                result.put("userName", jwtUtil.getUserNameFromToken(token));
                result.put("timeInfo", jwtUtil.getTokenTimeInfo(token));
                result.put("isExpiringSoon", jwtUtil.isTokenExpiringSoon(token));
                result.put("remainingTimeMs", jwtUtil.getRemainingTime(token));
            }
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("Token验证失败: ", e);
            return Result.error("Token验证失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取token的详细时间信息
     * @param token JWT token
     * @return 时间信息
     */
    @PostMapping("/timeinfo")
    public Result getTokenTimeInfo(@RequestParam String token) {
        try {
            Map<String, Object> timeInfo = jwtUtil.getTokenTimeInfo(token);
            return Result.success(timeInfo);
        } catch (Exception e) {
            log.error("获取Token时间信息失败: ", e);
            return Result.error("获取Token时间信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成测试token
     * @param userId 用户ID
     * @param userName 用户名
     * @return 生成的token
     */
    @PostMapping("/generate")
    public Result generateTestToken(@RequestParam Long userId, @RequestParam String userName) {
        try {
            String token = jwtUtil.generateToken(userId, userName);
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("timeInfo", jwtUtil.getTokenTimeInfo(token));
            
            return Result.success(result);
        } catch (Exception e) {
            log.error("生成测试Token失败: ", e);
            return Result.error("生成测试Token失败: " + e.getMessage());
        }
    }
}