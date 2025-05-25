package com.lzy.jshow.controller;

import com.lzy.jshow.constants.ResponseCode;
import com.lzy.jshow.constants.ResultCode;
import com.lzy.jshow.entity.AppUser;
import com.lzy.jshow.serivce.impl.AppUserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/usr")
@RestController
public class LoginController {
    @Autowired
    private AppUserServiceImpl appUserService;

    @PostMapping("/login")
    public ResponseEntity<ResponseCode> login(@RequestBody AppUser appUser,
                                              HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok().cacheControl(CacheControl.noCache())
                .body(new ResponseCode(ResultCode.SUCCESS,appUserService.login(appUser)));
    }
}
