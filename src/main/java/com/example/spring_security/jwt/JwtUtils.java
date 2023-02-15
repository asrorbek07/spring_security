package com.example.spring_security.jwt;

import com.example.spring_security.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
@Component
public class JwtUtils {
    private final static String jwtAccessKey = "thisIsTheSecretKey";
    private final static String jwtRefreshKey = "thisIsTheRefreshKey";
    private final static long expirationTimeOfAccessToken = 120000;
    private final static long expirationTimeOfRefreshToken = 7200000;

    public static synchronized String generateAccessToken(
            UserEntity userDetails
    ) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtAccessKey)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTimeOfAccessToken))
                .claim("authorities", userDetails.getAuthorities())
                .compact();
    }public static synchronized String generateRefreshToken(
            UserEntity userDetails
    ) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtRefreshKey)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTimeOfRefreshToken))
                .claim("authorities", userDetails.getAuthorities())
                .compact();
    }

    public synchronized static Claims isAccessTokenValid(String token) {
        return getAccessTokenClaim(token);
    }

    public synchronized static Claims isRefreshTokenValid(String token) {
        return getRefreshTokenClaim(token);
    }

//    public static List<String> getAuthorities(Claims claims) {
//        return (List<String>) claims.get("authorities");
//    }


    private static synchronized Claims getAccessTokenClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtAccessKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static synchronized Claims getRefreshTokenClaim(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtRefreshKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
