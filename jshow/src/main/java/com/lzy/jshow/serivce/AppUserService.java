package com.lzy.jshow.serivce;

import com.lzy.jshow.dto.AppUserDto;
import com.lzy.jshow.entity.AppUser;

public interface AppUserService {
    AppUser searchById(Long id);

    AppUserDto login(AppUser appUser);
}
