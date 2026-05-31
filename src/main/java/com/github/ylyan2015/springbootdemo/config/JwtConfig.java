package com.github.ylyan2015.springbootdemo.config;

import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;

/**
 * JWT配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret;

    private long expiration;

    private String header;

    private String tokenPrefix;

    @PostConstruct
    public void init() {
        if (secret == null || secret.isEmpty()) {
            secret = "springCloudDemoJwtSecretKey2026ForAuthenticationAndAuthorization";
        }
        if (expiration <= 0) {
            expiration = 7200;
        }
        if (header == null || header.isEmpty()) {
            header = "Authorization";
        }
        if (tokenPrefix == null || tokenPrefix.isEmpty()) {
            tokenPrefix = "Bearer ";
        }
    }

    public Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
