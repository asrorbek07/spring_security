package com.example.spring_security.entity;

import com.example.spring_security.model.RoleEnum;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRoleAuthority implements GrantedAuthority {
    private Integer id;

    private RoleEnum roleAuthority;

    public UserRoleAuthority(RoleEnum roleAuthority) {
        this.roleAuthority = roleAuthority;
    }
    public SimpleGrantedAuthority getSimpleGrantedAuthority(){
        return new SimpleGrantedAuthority("ROLE_"+roleAuthority.name());
    }

    @Override
    public String getAuthority() {
        return roleAuthority.name();
    }
}
