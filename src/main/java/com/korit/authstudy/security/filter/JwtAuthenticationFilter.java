package com.korit.authstudy.security.filter;

import com.korit.authstudy.domain.entity.User;
import com.korit.authstudy.repository.UsersRepository;
import com.korit.authstudy.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final UsersRepository usersRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("JWT AccessToken 검사");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authorization = request.getHeader("Authorization");
        System.out.println("Bearer 토큰:" + authorization);
        if (jwtUtil.isBearer(authorization)) {
            String accessToken = jwtUtil.removeBearer(authorization);
            try {
                Claims claims = jwtUtil.getClaims(accessToken);
                String id = claims.getId();
                Integer userId = Integer.parseInt(id);
                Optional<User> foundUserOptional =  usersRepository.findById(userId);

                foundUserOptional.ifPresentOrElse(user -> {}, ()->{});
//                foundUserOptional.ifPresentOrElse(new Consumer<User>() {  풀어쓰면 이거
//                    @Override
//                    public void accept(User user) {
//                    }
//                }, new Runnable(){
//                    @Override
//                            public void run(){
//                    }
//                });


            } catch (JwtException e) {
                e.printStackTrace();
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}

