package com.example.spring_security.filter;

import com.example.spring_security.jwt.JwtUtils;
import com.example.spring_security.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        if (!(requestHeader != null && requestHeader.startsWith("Bearer "))) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = requestHeader.replace("Bearer ", "");
        Claims claims = JwtUtils.isAccessTokenValid(token);

        if (claims == null) {
            filterChain.doFilter(request, response);
            return;
        }
        List<String> authorities = JwtUtils.getAuthorities(claims);

        authenticateUser(claims, authorities);

        filterChain.doFilter(request, response);
    }

    public void authenticateUser(Claims claims, List<String> authorities){
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), getAuthorities(authorities));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private List<SimpleGrantedAuthority> getAuthorities(List<String> permissions){
        List<SimpleGrantedAuthority> permissionList = new ArrayList<>();
        permissions.forEach((role)->{
            permissionList.add(new SimpleGrantedAuthority(role));
        });
        return permissionList;
    }
}
