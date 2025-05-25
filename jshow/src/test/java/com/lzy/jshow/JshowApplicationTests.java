package com.lzy.jshow;

import com.lzy.jshow.entity.AppUserExample;
import com.lzy.jshow.entity.SysPermission;
import com.lzy.jshow.mapper.AppUserMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
class JshowApplicationTests {

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Test
    void test01() {
        AppUserExample example = new AppUserExample();
        appUserMapper.countByExample(example);
    }

    @Test
    void test02(){
        System.out.println(bCryptPasswordEncoder.encode("1234"));;
    }

    @Test
    void test03(){
        Long id = 1L;
        List<String> sysPermissions = appUserMapper.permissionList(id);
        System.out.println("");
    }

}
