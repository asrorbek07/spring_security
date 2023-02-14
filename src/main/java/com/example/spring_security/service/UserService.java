package com.example.spring_security.service;

import com.example.spring_security.dto.UserLoginRequestDto;
import com.example.spring_security.dto.UserLoginResponseDto;
import com.example.spring_security.dto.UserRequestDto;
import com.example.spring_security.dto.UserResponseDto;
import com.example.spring_security.entity.UserEntity;
import com.example.spring_security.jwt.JwtUtils;
import com.example.spring_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto addUser(final UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            return null;
        }

        UserEntity userEntity = getUserEntity(userRequestDto);

        userRepository.save(userEntity);
        return getUserResponseDto(userEntity);

    }


    private UserResponseDto getUserResponseDto(UserEntity userEntity) {
        return UserResponseDto.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .email(userEntity.getEmail())
                .userPermissions(userEntity.getUserPermissions())
                .userRoles(userEntity.getUserRoles())
                .build();
    }

    private UserEntity getUserEntity(UserRequestDto userRequestDto) {
        return UserEntity.builder()
                .name(userRequestDto.getName())
                .username(userRequestDto.getUsername())
                .password(passwordEncoder.encode(userRequestDto.getPassword()))
                .email(userRequestDto.getEmail())
                .userPermissions(userRequestDto.getUserPermissions())
                .userRoles(userRequestDto.getUserRoles())
                .build();
    }

    public List<UserResponseDto> getList() {
        return userRepository.findAll().stream().map(userEntity -> getUserResponseDto(userEntity)).collect(Collectors.toList());
    }

    public UserLoginResponseDto login(UserLoginRequestDto userLoginRequestDto) {
        Optional<UserEntity> userRepositoryByUsername = userRepository.findByUsername(userLoginRequestDto.getUsername());
        if (!userRepositoryByUsername.isPresent()) return null;
        UserEntity userEntity = userRepositoryByUsername.get();
        String accessToken = JwtUtils.generateAccessToken(userEntity);
        String refreshToken = JwtUtils.generateRefreshToken(userEntity);
        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return userLoginResponseDto;
    }
}
