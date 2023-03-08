package com.tuchuan.face_recognition_acs.Filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@WebFilter(filterName = "LoginCheck",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取本次请求url
        String requestURL = request.getRequestURI();
        String[] whiteurls = new String[] {
                "/user/login",
                "/user/logout",
        };
        //2.判断本次请求是否需要处理
        boolean check = loginCheck(whiteurls,requestURL);
        //3.如果不需要处理直接放行
        if(check) {
            log.info("本次请求不用处理");
            filterChain.doFilter(request,response);
        }
        //4.判断登录状态，如果已经登录了，则放行
        String Xtoken = request.getHeader("X-token");
        log.info("token:{}",Xtoken);
    }
    private boolean loginCheck(String[] urls,String requestURL)
    {
        for(String url: urls) {
            boolean match = PATH_MATCHER.match(url,requestURL);
            if (match)
                return true;
        }
        return false;
    }
}
