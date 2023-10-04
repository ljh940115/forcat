package com.forcat.forcat.security;

import com.forcat.forcat.entity.Member;
import com.forcat.forcat.repository.MemberRepository;
import com.forcat.forcat.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2//로그 사용 명시
@Service//서비스 명시
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {//UserDetailsService : 실제 인증 처리 용도

/*    private PasswordEncoder passwordEncoder;

    public CustomUserDetailsService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }*/

    private final MemberRepository memberRepository;

    //username 값 사용자의 아이디 인증
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: "+username);

        /*//User 클래스를 이용해 임시  간단 로그인 처리
        UserDetails userDetails = User.builder()
                .username("user1")
                //.password("1234")
                *//*패스워드 인코딩 처리*//*
                .password(passwordEncoder.encode("1111"))
                .authorities("ROLE_USER")
                .build();*/

        Optional<Member> result = memberRepository.getWithRoles(username);

        if (result.isEmpty()) { //해당 아이디를 가진 사용자가 없다면
            throw new UsernameNotFoundException("username not found...");
        }

        Member member = result.get();

        MemberSecurityDTO memberSecurityDTO =
                new MemberSecurityDTO(
                        member.getMember_id(),
                        member.getMember_pw(),
                        member.getEmail(),
                        member.isDel(),
                        false,
                        member.getRoleSet()
                                .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name()))
                                .collect(Collectors.toList())
                );

        log.info("memberSecurityDTO");
        log.info(memberSecurityDTO);

        return memberSecurityDTO;
    }
}

