package com.example.spring_security.repository;


import com.example.spring_security.entity.UserEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> deleteByUsername(String username);
    Optional<UserEntity> findByUsernameAndPassword(String username, String password);
}