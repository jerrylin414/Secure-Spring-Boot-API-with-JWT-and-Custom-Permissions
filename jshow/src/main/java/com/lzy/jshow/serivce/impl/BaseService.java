package com.lzy.jshow.serivce.impl;

import com.lzy.jshow.config.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseService {
    @Autowired
    protected ConfigProperties config;
}
