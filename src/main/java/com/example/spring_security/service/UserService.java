package com.example.spring_security.service;

import com.example.spring_security.dto.*;
import com.example.spring_security.entity.UserEntity;
import com.example.spring_security.jwt.JsonHelper;
import com.example.spring_security.jwt.JwtUtils;
import com.example.spring_security.redis.UserSession;
import com.example.spring_security.redis.UserSessionRedisRepository;
import com.example.spring_security.repository.UserRepository;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.StreamHandler;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserService implements BaseService<UserRequestDto,String> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JsonHelper jsonHelper;
    private final UserSessionRedisRepository userSessionRedisRepository;


    @Override
    public ApiResponse add(UserRequestDto userRequestDto) {
        if (isUserExist(userRequestDto.getUsername()))
            return new ApiResponse(ResponseMessage.ERROR_USER_ALREADY_EXIST.getStatusCode(), ResponseMessage.ERROR_USER_ALREADY_EXIST.getMessage());

        UserEntity userEntity = getUserEntity(userRequestDto);

        userRepository.save(userEntity);
        return new ApiResponse(ResponseMessage.SUCCESS.getStatusCode(), ResponseMessage.SUCCESS.getMessage(),getUserResponseDto(userEntity));
    }

    @Override
    public ApiResponse delete(String userName) {
        if (isUserExist(userName)) {
            Optional<UserEntity> userEntity = userRepository.deleteByUsername(userName);
            return new ApiResponse(ResponseMessage.SUCCESS.getStatusCode(), ResponseMessage.SUCCESS.getMessage(),getUserResponseDto(userEntity.get()));
        }
        return new ApiResponse(ResponseMessage.ERROR_USER_NOT_FOUND.getStatusCode(), ResponseMessage.ERROR_USER_NOT_FOUND.getMessage());
    }

    @Override
    public ApiResponse list() {
        List<UserEntity> userEntities = userRepository.findAll();
        if (userEntities.isEmpty())
            return new ApiResponse(ResponseMessage.ERROR_USER_LIST_IS_EMPTY.getStatusCode(),ResponseMessage.ERROR_USER_LIST_IS_EMPTY.getMessage());
        return new ApiResponse(ResponseMessage.SUCCESS.getStatusCode(), ResponseMessage.SUCCESS.getMessage(),
                userEntities.stream().map(userEntity -> getUserResponseDto(userEntity)).collect(Collectors.toList()));
    }

    @Override
    public ApiResponse update(String userName, UserRequestDto userRequestDto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(userName);
        if (!optionalUserEntity.isPresent()) return new ApiResponse(ResponseMessage.ERROR_USER_ALREADY_EXIST.getStatusCode(), ResponseMessage.ERROR_USER_ALREADY_EXIST.getMessage());
        UserEntity userEntity = optionalUserEntity.get();
        changeUserEntity(userRequestDto,userEntity);
        userRepository.save(userEntity);
        return new ApiResponse(ResponseMessage.SUCCESS.getStatusCode(), ResponseMessage.SUCCESS.getMessage(),getUserResponseDto(userEntity));
    }

    private void changeUserEntity(UserRequestDto userRequestDto, UserEntity userEntity) {
        if (userRequestDto.getName()!=null&&!userRequestDto.getName().isEmpty()) userEntity.setName(userRequestDto.getName());
        if (userRequestDto.getUsername()!=null&&!userRequestDto.getUsername().isEmpty()) userEntity.setName(userRequestDto.getUsername());
        if (userRequestDto.getPassword()!=null&&!userRequestDto.getPassword().isEmpty()) userEntity.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        if (userRequestDto.getUserRoles()!=null&&!userRequestDto.getUserRoles().isEmpty()) userEntity.setUserRoles(userRequestDto.getUserRoles());
        if (userRequestDto.getUserPermissions()!=null&&!userRequestDto.getUserPermissions().isEmpty()) userEntity.setUserPermissions(userRequestDto.getUserPermissions());
        if (userRequestDto.getEmail()!=null&&!userRequestDto.getEmail().isEmpty()) userEntity.setEmail(userRequestDto.getEmail());
    }

    private boolean isUserExist(String userName) {
        return userRepository.findByUsername(userName).isPresent();
    }


    public ApiResponse login(UserLoginRequestDto userLoginRequestDto) {
        Optional<UserEntity> userRepositoryByUsername = userRepository.findByUsername(userLoginRequestDto.getUsername());
        if (!userRepositoryByUsername.isPresent()||!passwordEncoder.matches(userLoginRequestDto.getPassword(), userRepositoryByUsername.get().getPassword()))
            return new ApiResponse(ResponseMessage.ERROR_USER_NOT_FOUND.getStatusCode(), ResponseMessage.ERROR_USER_NOT_FOUND.getMessage());
        UserEntity userEntity = userRepositoryByUsername.get();
        String json = jsonHelper.getString(userEntity);
        UserSession userSession=null;
        Optional<UserSession> optionalUserSession = userSessionRedisRepository.findFirstByUserInfo(json);
        if (!optionalUserSession.isPresent()) {
            String uuid = UUID.randomUUID().toString();
            userSession = UserSession.builder()
                .id(uuid)
                .userInfo(json)
                .build();
        userSessionRedisRepository.save(userSession);
        } else{
            userSession=optionalUserSession.get();
        }

        String accessToken = JwtUtils.generateAccessToken(userSession.getId());
        String refreshToken = JwtUtils.generateRefreshToken(userSession.getId());
        UserLoginResponseDto userLoginResponseDto = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return new ApiResponse(ResponseMessage.SUCCESS.getStatusCode(), ResponseMessage.SUCCESS.getMessage(),userLoginResponseDto);
    }


    private UserResponseDto getUserResponseDto(UserEntity userEntity) {
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .name(userEntity.getName())
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



    public ApiResponse getByUserName(String username) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByUsername(username);
        if (!optionalUserEntity.isPresent())  return new ApiResponse(ResponseMessage.ERROR_USER_NOT_FOUND.getStatusCode(), ResponseMessage.ERROR_USER_NOT_FOUND.getMessage());
        return new ApiResponse(ResponseMessage.SUCCESS.getStatusCode(),ResponseMessage.SUCCESS.getMessage(), getUserResponseDto(optionalUserEntity.get()));
    }

    public ApiResponse getAccessToken(String refreshToken) {
        Claims claims = JwtUtils.isRefreshTokenValid(refreshToken);
        if (claims != null) {
            String uuid = claims.getSubject();
            Optional<UserSession> byId = userSessionRedisRepository.findById(uuid);
            String userInfo = byId.get().getUserInfo();
            UserEntity userEntity = jsonHelper.getUserEntity(userInfo);
            if (userEntity != null) {
                return new ApiResponse(
                        0,
                        "success",
                        Map.of(
                                "access_token", JwtUtils.generateAccessToken(uuid)
                        )
                );
            }
        }
        return null;
    }
}

