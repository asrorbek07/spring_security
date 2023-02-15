package com.example.spring_security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private final static String jwtAccessKey = "thisIsTheSecretKey";
    private final static String jwtRefreshKey = "thisIsTheRefreshKey";
    private final static long expirationTimeOfAccessToken = 120000;
    private final static long expirationTimeOfRefreshToken = 7200000;

    public static synchronized String generateAccessToken(
            String uuid
    ) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtAccessKey)
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTimeOfAccessToken))
                .compact();
    }public static synchronized String generateRefreshToken(
            String uuid
    ) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, jwtRefreshKey)
                .setSubject(uuid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTimeOfRefreshToken))
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
