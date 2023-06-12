package com.nowcoder.community.config;

import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * ClassName: SecurityConfig
 * Package: com.nowcoder.community.config
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/9 18:54
 * @Version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig implements CommunityConstant {

    /**
     * 静态资源不做认证
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 授权
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(
                                        "/user/setting",
                                        "/user/upload",
                                        "/discuss/add",
                                        "/comment/add/**",
                                        "/letter/**",
                                        "/notice/**",
                                        "/like",
                                        "/follow",
                                        "/unfollow"
                                )
                                .hasAnyAuthority(
                                        AUTHORITY_USER,
                                        AUTHORITY_ADMIN,
                                        AUTHORITY_MODERATOR
                                )
                                .requestMatchers(
                                        "/discuss/top",
                                        "/discuss/wonderful"
                                )
                                .hasAnyAuthority(
                                        AUTHORITY_MODERATOR
                                )
                                .requestMatchers(
                                        "/discuss/delete",
                                        "/data/**"
                                )
                                .hasAnyAuthority(
                                        AUTHORITY_ADMIN
                                )
                                .anyRequest()
                                .permitAll()
                // 图省事，将csrf关闭
        ).csrf((csrf) -> csrf.disable());

        // 权限不够的时候处理
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            // 没有登陆
                            @Override
                            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                                String xRequestedWith = request.getHeader("x-requested-with");
                                if ("XMLHttpRequest".equals(xRequestedWith)) {
                                    response.setContentType("application/plain;charset=utf-8");
                                    PrintWriter writer = response.getWriter();
                                    writer.write(CommunityUtil.getJSONString(403, "请您先登陆呢~"));
                                } else {
                                    response.sendRedirect(request.getContextPath() + "/login");
                                }
                            }
                        })
                        .accessDeniedHandler(new AccessDeniedHandler() {
                            // 权限不足
                            @Override
                            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                                String xRequestedWith = request.getHeader("x-requested-with");
                                if ("XMLHttpRequest".equals(xRequestedWith)) {
                                    response.setContentType("application/plain;charset=utf-8");
                                    PrintWriter writer = response.getWriter();
                                    writer.write(CommunityUtil.getJSONString(403, "你没有访问此功能的权限!"));
                                } else {
                                    response.sendRedirect(request.getContextPath() + "/denide");
                                }
                            }
                        })
        );

        // Security 底层默认会拦截 /logout 请求，进行退出的处理。
        // 我们覆盖它默认的逻辑，才能执行我们自己退出的代码
        http.logout((logout) ->
                logout.logoutUrl("/securitylogout")
        );

        return http.build();
    }

    /**
     * 注入持久化SecurityContext的securityContextRepository
     * @return
     */
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

}
