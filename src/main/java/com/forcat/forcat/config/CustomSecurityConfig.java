package com.forcat.forcat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2//로그 사용 명시
@Configuration//환경설정 클래스 명시
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)//어노테이션으로 권한 설정 @PreAuthorize() 사용
public class CustomSecurityConfig {

    /*비밀번호 암호화 처리*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*필터 체인을 이용해 경로 접근*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("==========환경 설정==========");

        //formLogin:Form 로그인 기능 사용, loginPage:로그인이 필요한 경우 리다이렉트
        http.formLogin().loginPage("/member/login");
        return http.build();
    }

    /*정적 자원 필터 처리 제외*/
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("==========웹 환경 설정==========");
     return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

}