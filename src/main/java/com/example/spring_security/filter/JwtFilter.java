package com.example.spring_security.filter;

import com.example.spring_security.entity.UserEntity;
import com.example.spring_security.jwt.JwtUtils;
import com.example.spring_security.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collection;

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
        UserEntity userEntity = userRepository.findByUsername(claims.getSubject()).get();
        Collection<? extends GrantedAuthority> authorities = userEntity.getAuthorities();

        authenticateUser(userEntity,request);

        filterChain.doFilter(request, response);
    }

    public void authenticateUser(UserEntity userEntity, HttpServletRequest request){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userEntity, null, userEntity.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

}
