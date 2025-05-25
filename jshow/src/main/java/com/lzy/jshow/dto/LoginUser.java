package com.lzy.jshow.dto;

import com.auth0.jwt.JWT;
import com.lzy.jshow.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;



public class LoginUser implements UserDetails {
    private AppUserDto appUserDto;

    private List<String> permissions;

//    @JSONField(serizlize = false)
    List<GrantedAuthority> authorities;



    public LoginUser() {
    }

    public LoginUser(AppUserDto appUserDto, List<String> permissions) {
        this.appUserDto = appUserDto;
        this.permissions = permissions;
    }

    public AppUserDto getAppUserDto() {
        return appUserDto;
    }

    public void setAppUserDto(AppUserDto appUserDto) {
        this.appUserDto = appUserDto;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities != null){
            return authorities;
        }
        authorities = permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return appUserDto.getPassword();
    }

    @Override
    public String getUsername() {
        return appUserDto.getUsername();
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
