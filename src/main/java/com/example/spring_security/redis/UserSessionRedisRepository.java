package com.example.spring_security.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRedisRepository extends CrudRepository<UserSession, String> {
  Optional<UserSession> findFirstByUserInfo(String userInfo);
}
