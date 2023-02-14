package com.example.spring_security.entity;

import com.example.spring_security.model.PermissionEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserPermissionAuthority implements GrantedAuthority {
    private Integer id;
private PermissionEnum permissionEnumAuthority;

    public UserPermissionAuthority(PermissionEnum permissionEnumAuthority) {
        this.permissionEnumAuthority = permissionEnumAuthority;
    }
    @JsonIgnore
public SimpleGrantedAuthority getSimpleGrantedAuthority(){
        return new SimpleGrantedAuthority(permissionEnumAuthority.name());
}

    @Override
    public String getAuthority() {
        return permissionEnumAuthority.name();
    }
}
