package com.lzy.jshow.serivce.impl;

import com.lzy.jshow.constants.ResultCode;
import com.lzy.jshow.dto.AppUserDto;
import com.lzy.jshow.dto.LoginUser;
import com.lzy.jshow.entity.AppUser;
import com.lzy.jshow.entity.AppUserExample;
import com.lzy.jshow.entity.SysPermission;
import com.lzy.jshow.mapper.AppUserMapper;
import com.lzy.jshow.utils.CommonUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserExample userExample = new AppUserExample();
        userExample.createCriteria().andUsernameEqualTo(username);
        List<AppUser> list = appUserMapper.selectByExample(userExample);
        AppUser first = CommonUtils.getFirst(list);

        if (first == null){
            throw new RuntimeException("Username or Password Error!");
        }
        AppUserDto userDto = new AppUserDto();
        BeanUtils.copyProperties(first,userDto);

        List<String> permissions = appUserMapper.permissionList(userDto.getId());
        return new LoginUser(userDto,permissions);
    }
}
