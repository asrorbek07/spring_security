package com.example.spring_security.dto;

import com.example.spring_security.model.PermissionEnum;
import com.example.spring_security.model.RoleEnum;
import lombok.Data;

import java.util.List;


@Data
public class UserRequestDto {

    private String username;
    private String password;
private String email;
    private String name;

    private List<PermissionEnum> userPermissions;
    private List<RoleEnum> userRoles;
}
