package com.example.spring_security.entity;


import com.example.spring_security.model.PermissionEnum;
import com.example.spring_security.model.RoleEnum;
//import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document()
public class UserEntity implements UserDetails {

    private String name;
    private String username;
    private String password;
    private String email;
    private List<PermissionEnum> userPermissions;


    private List<RoleEnum> userRoles;

    @SneakyThrows
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        simpleGrantedAuthorities.addAll(userPermissions.stream().map(userPermissionAuthority -> new SimpleGrantedAuthority(userPermissionAuthority.name())).collect(Collectors.toList()));
        simpleGrantedAuthorities.addAll(userPermissions.stream().map(userRoleAuthority -> new SimpleGrantedAuthority("Role_"+userRoleAuthority.name())).collect(Collectors.toList()));
        return simpleGrantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
