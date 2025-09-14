package com.example.userservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和验证JWT token
 */
@Component
public class JwtUtil {
    
    // JWT密钥
    private static final String SECRET_KEY = "smart-campus-secret-key-for-jwt-token-generation-2024";
    
    // token过期时间（24小时）
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    
    // 生成密钥
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    
    /**
     * 生成JWT token
     * @param userId 用户ID
     * @param userName 用户名
     * @return JWT token
     */
    public String generateToken(Long userId, String userName) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userName", userName);
        claims.put("iat", now.getTime()); // 签发时间戳
        claims.put("exp", expirationDate.getTime()); // 过期时间戳
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * 从token中获取用户名
     * @param token JWT token
     * @return 用户名
     */
    public String getUserNameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }
    
    /**
     * 从token中获取用户ID
     * @param token JWT token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * 验证token是否有效
     * @param token JWT token
     * @return 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date now = new Date();
            // 检查token是否过期
            return !claims.getExpiration().before(now);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 检查token是否即将过期（1小时内）
     * @param token JWT token
     * @return 是否即将过期
     */
    public boolean isTokenExpiringSoon(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            long timeUntilExpiration = expirationDate.getTime() - now.getTime();
            // 如果剩余时间少于1小时（3600000毫秒），则认为即将过期
            return timeUntilExpiration < 3600000;
        } catch (Exception e) {
            return true; // 如果解析失败，认为即将过期
        }
    }
    
    /**
     * 获取token的剩余有效时间（毫秒）
     * @param token JWT token
     * @return 剩余时间（毫秒），如果已过期返回0
     */
    public long getRemainingTime(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expirationDate = claims.getExpiration();
            Date now = new Date();
            long remainingTime = expirationDate.getTime() - now.getTime();
            return Math.max(0, remainingTime);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * 从token中获取Claims
     * @param token JWT token
     * @return Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 获取token过期时间
     * @param token JWT token
     * @return 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
    
    /**
     * 获取token签发时间
     * @param token JWT token
     * @return 签发时间
     */
    public Date getIssuedAtFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getIssuedAt();
    }
    
    /**
     * 获取token的详细时间信息
     * @param token JWT token
     * @return 包含时间信息的Map
     */
    public Map<String, Object> getTokenTimeInfo(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date now = new Date();
            Date issuedAt = claims.getIssuedAt();
            Date expiration = claims.getExpiration();
            
            Map<String, Object> timeInfo = new HashMap<>();
            timeInfo.put("issuedAt", issuedAt);
            timeInfo.put("expiration", expiration);
            timeInfo.put("currentTime", now);
            timeInfo.put("isExpired", expiration.before(now));
            timeInfo.put("remainingTimeMs", Math.max(0, expiration.getTime() - now.getTime()));
            timeInfo.put("ageMs", now.getTime() - issuedAt.getTime());
            
            return timeInfo;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}