package com.forcat.forcat.repository;

import com.forcat.forcat.entity.Member;
import com.forcat.forcat.entity.MemberRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test// 회원 추가 테스트
    public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i -> {

            Member member = Member.builder()
                    .mid("memberTEST"+i)
                    .mpw(passwordEncoder.encode("1111"))
                    .email("email"+i+"@example.com")
                    .build();

            member.addRole(MemberRole.USER);

            if(i >= 90){
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }//회원 추가 메서드 종료

    @Test//회원 조회 메서드
    public void testRead(){
        //Optional을 이용해 null 예외처리 방지
        Optional<Member> result = memberRepository.getWithRoles("memberTEST100");
        //예외 발생 시 member에 값을 저장
        Member member = result.orElseThrow();
        log.info("==========회원 조회==========");
        log.info(member);//멤버 정보 출력
        log.info(member.getRoleSet());//멤버 권한 출력

        member.getRoleSet().forEach(memberRole -> log.info(memberRole.name()));

    }

}
