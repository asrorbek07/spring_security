package com.example.spring_security.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash
public class UserSession {

    @Id
    private String id;
    private String userInfo;
}
