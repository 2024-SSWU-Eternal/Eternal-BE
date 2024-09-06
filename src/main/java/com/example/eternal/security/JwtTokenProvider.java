package com.example.eternal.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // HS512 알고리즘에 적합한 비밀키 생성
    private final SecretKey JWT_SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final long JWT_EXPIRATION = 604800000L; // 토큰 유효 기간 (7일)

    // JWT 토큰 생성 - 학번 포함
    public String generateToken(String email, Integer studentNumber) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(email) // 이메일을 주체로 설정
                .claim("student_num", studentNumber) // 학번을 클레임에 포함
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(JWT_SECRET, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT 토큰에서 이메일 추출
    public String getEmailFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    // JWT 토큰에서 학번 추출
    public Integer getStudentNumFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Long으로 저장될 가능성이 있으므로 Integer로 변환 처리
        return claims.get("student_num", Integer.class);
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(JWT_SECRET).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
