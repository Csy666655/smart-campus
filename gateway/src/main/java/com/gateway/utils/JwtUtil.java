package com.gateway.utils;

import com.common.config.UnauthorizedException;
import io.jsonwebtoken.*;
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
     */
    public String generateToken(Long userId, String userName) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("userName", userName);

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
     */
    public String getUserNameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 从token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new UnauthorizedException("未登录");
        }
        return getClaimsFromToken(token).get("userId", Long.class);
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("token已过期", e);
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException("不支持的token", e);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException("非法的token", e);
        } catch (SignatureException e) {
            throw new UnauthorizedException("token签名错误", e);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("token参数无效", e);
        }
    }

    /**
     * 检查token是否即将过期（1小时内）
     */
    public boolean isTokenExpiringSoon(String token) {
        Claims claims = getClaimsFromToken(token);
        long timeUntilExpiration = claims.getExpiration().getTime() - System.currentTimeMillis();
        return timeUntilExpiration < 3600000;
    }

    /**
     * 获取token的剩余有效时间（毫秒）
     */
    public long getRemainingTime(String token) {
        Claims claims = getClaimsFromToken(token);
        long remainingTime = claims.getExpiration().getTime() - System.currentTimeMillis();
        return Math.max(0, remainingTime);
    }

    /**
     * 从token中获取Claims（带异常处理）
     */
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("token已过期", e);
        } catch (UnsupportedJwtException e) {
            throw new UnauthorizedException("不支持的token", e);
        } catch (MalformedJwtException e) {
            throw new UnauthorizedException("非法的token", e);
        } catch (SignatureException e) {
            throw new UnauthorizedException("token签名错误", e);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("token参数无效", e);
        }
    }

    /**
     * 获取token过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }

    /**
     * 获取token签发时间
     */
    public Date getIssuedAtFromToken(String token) {
        return getClaimsFromToken(token).getIssuedAt();
    }

    /**
     * 获取token的详细时间信息
     */
    public Map<String, Object> getTokenTimeInfo(String token) {
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
    }
}
