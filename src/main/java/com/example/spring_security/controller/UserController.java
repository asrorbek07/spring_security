package com.example.spring_security.controller;

import com.example.spring_security.dto.UserLoginRequestDto;
import com.example.spring_security.dto.UserLoginResponseDto;
import com.example.spring_security.dto.UserRequestDto;
import com.example.spring_security.dto.UserResponseDto;
import com.example.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public List<UserResponseDto> getUserList() {
        return userService.getList();
    }

    @PostMapping("/add")
    public UserResponseDto addUser(
            @RequestBody UserRequestDto userRequestDto) {
        return userService.addUser(userRequestDto);
    }
    @PostMapping("/login")
    public UserLoginResponseDto login(
            @RequestBody UserLoginRequestDto userLoginRequestDto
    ){
       return userService.login(userLoginRequestDto);
    }
}
