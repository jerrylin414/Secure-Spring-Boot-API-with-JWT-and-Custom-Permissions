package com.lzy.jshow.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class AppManageController {

    @RequestMapping("/hello")
    @PreAuthorize("@cs.hasAnyAuthority('user:view', 'admin:all')")//user and admin都有權力
    public String hello(){
        System.out.println("hello");
        return "hello";
    }

    @RequestMapping("/bye")
    @PreAuthorize("hasAnyAuthority('admin:all')")//admin現有權力
    public String bye(){
        System.out.println("bye");
        return "bye";
    }

}
