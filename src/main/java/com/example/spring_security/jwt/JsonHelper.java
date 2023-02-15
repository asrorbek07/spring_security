package com.example.spring_security.jwt;

import com.example.spring_security.entity.UserEntity;
import com.example.spring_security.redis.UserSessionRedisRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonHelper {
    private final Gson gson;
    private final UserSessionRedisRepository userSessionRedisRepository;

    public String getString(UserEntity userEntity){
        return gson.toJson(userEntity);
    }
    public UserEntity getUserEntity(String userInfo){
        return gson.fromJson(userInfo,UserEntity.class);
    }
}
