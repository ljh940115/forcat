package com.forcat.forcat.config;

import com.forcat.forcat.security.CustomUserDetailsService;
import com.forcat.forcat.security.handler.CustomSocialLoginSuccessHandler;
import com.forcat.forcat.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.multipart.support.MultipartFilter;

import javax.sql.DataSource;

@Log4j2//로그 사용
@Configuration
@AllArgsConstructor//해당 필드에 대한 모든 생성자 생성
@EnableGlobalMethodSecurity (prePostEnabled = true)//권한 설정 @PreAuthorize() 사용
public class CustomSecurityConfig {//보안 환경설정 클래스

    private final DataSource dataSource;//DB 연결
    private final CustomUserDetailsService userDetailsService;//커스텀 사용자 서비스 클래스

    @Bean//비밀번호 암호화 처리
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder ();
    }

    @Bean//필터 체인 설정 : URL 경로 및 보안 설정 정의
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        log.info ("==========필터 체인 환경 설정");
        /*CSRF 토큰 비활성화
        http.csrf().disable();*/
        /*http.csrf().ignoringAntMatchers("/upload", "/board/register");*/
        /*로그아웃 구성 : CSRF 토큰 비활성화 상태에서 사용
        http.logout () // 로그아웃 기능 작동함
                .logoutUrl ("/member/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
                .logoutSuccessUrl ("/member/login") // 로그아웃 성공 후 이동페이지
                .deleteCookies ("JSESSIONID", "remember-me"); // 로그아웃 후 쿠키 삭제*/

        http.authorizeRequests() // 요청에 대한 인증 및 권한 설정
                .mvcMatchers("/", "/index/**", "/content/**", "/board/**", "/member/**", "/replies/**", "/item/**", "/shop/**", "/view/**", "/upload/**", "/remove/**").permitAll() // permitAll() : 모든 사용자 접근 허용
                .mvcMatchers("/static/**").permitAll()
                .mvcMatchers("/upload").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN") // ADMIN Role인 사용자만 접근 허용
                .anyRequest().authenticated() // 그 외 요청은 인증된 사용자만 가능
                .and()
                .csrf()
                .csrfTokenRepository(csrfTokenRepository()); // CSRF 토큰을 사용할 URL을 지정

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
        // 인증되지 않은 사용자의 접근 시 수행되는 핸들러 -> CustomAuthenticationEntryPoint로 보냄

        //커스텀 로그인 페이지
        http.formLogin ().loginPage ("/member/login").defaultSuccessUrl ("/")//로그인 성공 처리
                .usernameParameter ("username")//로그인 시 사용할 파라미터 이름
                .failureUrl ("/member/login/error")
                .and().logout ().logoutRequestMatcher (new AntPathRequestMatcher ("/member/logout")).logoutSuccessUrl("/");
                /*.failureForwardUrl ("/member/login").and ().logout ().logoutRequestMatcher//로그인 실패 시 리디렉션 위치
                        (new AntPathRequestMatcher ("/member/logout")).logoutSuccessUrl//로그아웃 수행 위치
                        ("/member/login").deleteCookies ("JSESSIONID", "remember-me");//로그인 실패 처리*/

        //자동 로그인 설정
        http.rememberMe ().key ("12345678")//토큰 검증용 키
                .tokenRepository (persistentTokenRepository ())//토큰 저장 관리
                .userDetailsService (userDetailsService)//자동 로그인 사용할 때 사용자 정보 확인
                .tokenValiditySeconds (60 * 60 * 24 * 30);//유효 토큰 기간 60초, 60분, 24시간, 30일

        //OAuth2 소셜 로그인 사용
        http.oauth2Login ().loginPage ("/member/login").successHandler (authenticationSuccessHandler ());
        return http.build ();
    }


    @Bean//보안 방화벽 설정 : "//"와 같은 형태의 URL도 정상적으로 처리
    public HttpFirewall allowUrlEncodedSlashHttpFirewall () {
        log.info ("==========보안 방화벽 확인");
        DefaultHttpFirewall firewall = new DefaultHttpFirewall ();
        firewall.setAllowUrlEncodedSlash (true); // 이 부분을 true로 설정하여 "//"를 "/"로 허용합니다.
        return firewall;
    }

    @Bean//자동 로그인을 위한 토큰 저장 관리 설정
    public PersistentTokenRepository persistentTokenRepository () {
        log.info ("==========자동 로그인 쿠키 확인");
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl ();
        repo.setDataSource (dataSource);
        return repo;
    }

    @Bean//소셜 로그인 성공 시 처리 : 암호 인코딩
    public AuthenticationSuccessHandler authenticationSuccessHandler () {
        log.info ("==========소셜 로그인 암호 인코딩");
        return new CustomSocialLoginSuccessHandler (passwordEncoder ());
    }

    @Bean//정적 자원 요청 무시 : 시큐리티 처리하지 않음
    public WebSecurityCustomizer webSecurityCustomizer () {
        log.info ("==========시큐리티 예외");
        return (web) -> web.ignoring ().requestMatchers (PathRequest.toStaticResources ().atCommonLocations ());
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN"); // CSRF 토큰의 헤더 이름
        return repository;
    }
}