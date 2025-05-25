package com.lzy.jshow.config;


import com.lzy.jshow.dto.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("cs")
public class CustomAuthority {
    public boolean hasAnyAuthority(String ... authority){
        System.out.println("custom check authority...");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        if (permissions != null){
            for (String permission : permissions) {
                if (permission.contains("admin")){
                    return true;
                }
            }
        }
        return permissions.contains(authority);
    }
}
