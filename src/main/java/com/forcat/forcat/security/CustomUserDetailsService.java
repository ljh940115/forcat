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

    private final MemberRepository memberRepository;

    @Override//username 값 사용자의 아이디 인증
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: "+username);
        Optional<Member> result = memberRepository.getWithRoles(username);
        if (result.isEmpty()) { //해당 아이디를 가진 사용자가 없다면
            throw new UsernameNotFoundException("username not found...");
        }
        Member member = result.get();
        MemberSecurityDTO memberSecurityDTO =
                new MemberSecurityDTO(
                        member.getMid(),
                        member.getMpw(),
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

