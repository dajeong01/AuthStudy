package com.korit.authstudy.filter;

import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StudyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("전처리");
        // doFilter 위까지는 전처리
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("후처리");
        // doFilter 밑이니 여기부터는 이제 후처리
    }
}
