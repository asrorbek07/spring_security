package com.example.spring_security.dto;

import com.example.spring_security.model.PermissionEnum;
import com.example.spring_security.model.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String id;

    private String name;
    private String username;
    private String password;
    private String email;
    private List<PermissionEnum> userPermissions;
    private List<RoleEnum> userRoles;
}
