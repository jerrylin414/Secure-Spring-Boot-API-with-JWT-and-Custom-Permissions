package com.lzy.jshow.filter;

import com.lzy.jshow.serivce.impl.AppUserServiceImpl;
import com.lzy.jshow.serivce.impl.SysRoleServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecruityPathFilter extends OncePerRequestFilter {

    @Autowired
    private SysRoleServiceImpl sysRoleService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO don't need this filter
        filterChain.doFilter(request,response);
    }
}
