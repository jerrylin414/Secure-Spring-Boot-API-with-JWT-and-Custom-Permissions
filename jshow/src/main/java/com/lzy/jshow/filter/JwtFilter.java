package com.lzy.jshow.filter;

import com.alibaba.excel.util.StringUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lzy.jshow.config.ConfigProperties;
import com.lzy.jshow.dto.AppUserDto;
import com.lzy.jshow.dto.LoginUser;
import com.lzy.jshow.entity.AppUser;
import com.lzy.jshow.entity.AppUserExample;
import com.lzy.jshow.mapper.AppUserMapper;
import com.lzy.jshow.serivce.impl.AppUserServiceImpl;
import com.lzy.jshow.utils.CommonUtils;
import com.lzy.jshow.utils.TokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//Filter to login service
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private ConfigProperties configProperties;

    @Autowired
    private AppUserMapper appUserMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            DecodedJWT decodedJWT = TokenUtils.verify(token, configProperties.getTokenSign());
            String userId = decodedJWT.getAudience().get(0);
            String username = decodedJWT.getClaim("username").asString();
            List<String> permissions = decodedJWT.getClaim("permissions").asList(String.class);

            AppUserDto userDto = new AppUserDto();
            userDto.setId(Long.valueOf(userId));
            userDto.setUsername(username);

            LoginUser loginUser = new LoginUser(userDto, permissions);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception e) {
            throw new RuntimeException("invalid token");
        }
        filterChain.doFilter(request, response);
    }
}
