package com.tourlesjours.calendar.config;

import com.tourlesjours.calendar.member.security.MemberAccessDeniedHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity      // security 정책을 웹에 적용하겠다.
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable());


        http
                // 요청에 대한 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/css/*",
                                "/img/*",
                                "/js/*",
                                "/member/signup",
                                "/member/signup_confirm",
                                "/member/signin",
                                "/member/signin_result",
                                "/member/findpassword",
                                "/member/findpassword_confirm").permitAll()
                        .requestMatchers("/planner/**").hasAnyRole("USER")
                        // 위 허용 외에 모든 요청에 대해 인증하도록 설정.
                        .anyRequest().authenticated()
                );

        http
                // 로그인 요청 설정
                .formLogin(login -> login
                        .loginPage("/member/signin")
                        .loginProcessingUrl("/member/signin_confirm")
                        .usernameParameter("id")
                        .passwordParameter("pw")
                        .successHandler((request, response, authentication) -> {
                            log.info("signin successHandler()");

                            User user = (User) authentication.getPrincipal();
                            String targetURI = "/member/signin_result?loginedId=" + user.getUsername();
                            response.sendRedirect(targetURI);

                        })
                        .failureHandler(((request, response, exception) -> {
                            log.info("signin failureHandler()");

                            String targetURI = "/member/signin_result";
                            response.sendRedirect(targetURI);
                        }))
                );

        http
                // 로그아웃 요청 설정
                .logout(logout -> logout
                        .logoutUrl("/member/signout_confirm")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            log.info("signout logout successHandler()");

                            String targetURI = "/";
                            response.sendRedirect(targetURI);
                        })
                );

        http
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new MemberAccessDeniedHandler()));

        return http.build();
    }
}
