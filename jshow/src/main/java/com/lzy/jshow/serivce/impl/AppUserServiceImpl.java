package com.lzy.jshow.serivce.impl;

import com.lzy.jshow.dto.AppUserDto;
import com.lzy.jshow.dto.LoginUser;
import com.lzy.jshow.entity.AppUser;
import com.lzy.jshow.entity.AppUserExample;
import com.lzy.jshow.mapper.AppUserMapper;
import com.lzy.jshow.serivce.AppUserService;
import com.lzy.jshow.utils.CommonUtils;
import com.lzy.jshow.utils.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserServiceImpl extends BaseService implements AppUserService {
    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public AppUser searchById(Long id) {
        AppUserExample ex = new AppUserExample();
        ex.createCriteria().andIdEqualTo(id);
        List<AppUser> appUsers = appUserMapper.selectByExample(ex);
        return CommonUtils.getFirst(appUsers);
    }

    @Override
    public AppUserDto login(AppUser appUser) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword());
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);//UserDetails -> loadUserByUsername
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed, invalid username or password.");
        }
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long id = loginUser.getAppUserDto().getId();
        String jwt = TokenUtils.getToken(id, config.getTokenSign(), appUser.getUsername(), loginUser.getPermissions());
        AppUserDto userDto = loginUser.getAppUserDto();
        userDto.setPassword(null);
        userDto.setToken(jwt);
        return userDto;
    }
}
