package com.example.spring_security.controller;

import com.example.spring_security.dto.UserLoginRequestDto;
import com.example.spring_security.dto.UserLoginResponseDto;
import com.example.spring_security.dto.UserRequestDto;
import com.example.spring_security.dto.UserResponseDto;
import com.example.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<?> getUserList() {
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.list());
    }
@GetMapping("get")
public ResponseEntity<?> getUserBYUserName(
        @RequestParam String username
){
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getByUserName(username));
}
    @PostMapping("/add")
    public ResponseEntity<?> addUser(
            @RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.add(userRequestDto));
    }
    @GetMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserLoginRequestDto userLoginRequestDto
    ){
       return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.login(userLoginRequestDto));
    }
    @PutMapping("update")
    public ResponseEntity<?> updateUser(
            @RequestBody UserRequestDto userRequestDto,
            @RequestParam String username
    ){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.update(username,userRequestDto));
    }
    @GetMapping("/delete")
    public ResponseEntity<?> deleteUser(
            @RequestParam String username
    ){
        return ResponseEntity.status(HttpStatus.GONE).body(userService.delete(username));
    }
    @GetMapping("/getUser")
    public ResponseEntity<?> getByUsername(
            @RequestParam String username
    ){
        return ResponseEntity.status(HttpStatus.FOUND).body(userService.getByUserName(username));
    }
}
