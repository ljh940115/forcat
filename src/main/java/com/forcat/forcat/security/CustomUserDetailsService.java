package com.forcat.forcat.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2//로그 사용 명시
@Service//서비스 명시
public class CustomUserDetailsService implements UserDetailsService {//UserDetailsService : 실제 인증 처리 용도

    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    //username 값 사용자의 아이디 인증
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: "+username);

        //User 클래스를 이용해 임시  간단 로그인 처리
        UserDetails userDetails = User.builder()
                .username("user1")
                //.password("1234")
                /*패스워드 인코딩 처리*/
                .password(passwordEncoder.encode("1111"))
                .authorities("ROLE_USER")
                .build();

        return userDetails;
        }
}

