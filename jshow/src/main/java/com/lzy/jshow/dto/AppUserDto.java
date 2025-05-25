package com.lzy.jshow.dto;

import com.lzy.jshow.entity.AppUser;
import lombok.Data;

@Data
public class AppUserDto extends AppUser {
    private String token;
}
