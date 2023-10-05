package com.forcat.forcat.config;

import com.forcat.forcat.security.CustomUserDetailsService;
import com.forcat.forcat.security.handler.CustomSocialLoginSuccessHandler;
import lombok.AllArgsConstructor;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2//로그 사용 명시
@Configuration//환경설정 클래스 명시
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)//어노테이션으로 권한 설정 @PreAuthorize() 사용
public class CustomSecurityConfig{

    //자동 로그인을 위한 주입
    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;

    /*비밀번호 암호화 처리*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*필터 체인을 이용해 경로 접근*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("==========환경 설정==========");

        //커스텀 로그인 페이지 사용
        //formLogin:Form 로그인 기능 사용, loginPage:로그인이 필요한 경우 리다이렉트
        http.formLogin().loginPage("/member/login");
        //CSRF 토큰 비활성화
        http.csrf().disable();

        //자동 로그인 설정
        http.rememberMe()
                .key("12345678")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60*60*24*30);

        //OAuth2 로그인 사용 명시
        http.oauth2Login().loginPage("/member/login").successHandler(authenticationSuccessHandler());

        // 로그아웃 구성
        http.logout() // 로그아웃 기능 작동함
                .logoutUrl("/member/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
                .logoutSuccessUrl("/member/login") // 로그아웃 성공 후 이동페이지
                .deleteCookies("JSESSIONID", "remember-me");// 로그아웃 후 쿠키 삭제
        return http.build();
    }

    /*정적 자원 필터 처리 제외*/
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("==========웹 환경 설정==========");
     return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    //쿠키 생성 시 쿠키 값 인코딩하기 위한 키 값과 정보 저장
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    //커스텀 소셜 로그인 성공 시 처리, 암호 인코딩
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }
}