package com.lzy.jshow.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.lzy.jshow.mapper")
public class MyBatisConfig {
}
