package com.forcat.forcat.repository;

import com.forcat.forcat.dto.MemberJoinDTO;
import com.forcat.forcat.entity.Member;
import com.forcat.forcat.entity.MemberRole;
import com.forcat.forcat.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test// 회원 추가 테스트
    public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i -> {

            Member member = Member.builder()
                    .member_id("member"+i)
                    .member_pw(passwordEncoder.encode("1111"))
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

    @Commit
    @Test
    public void testUpdate(){
        String member_id = "ljh940115@kakao.com";//소셜 로그인으로 추가된 사용자로 현재 DB에 존재하는 이메일
        String member_pw = passwordEncoder.encode("54321");

        log.info("member_id : " + member_id);
        log.info("member_pw : " + member_pw);
        memberRepository.updatePassword(member_pw, member_id);
    }

    @Test
    @DisplayName("회원 추가 테스트2")
    public Member insertMembers2() {
        Member member = Member.builder()
                .member_id("memberid")
                .member_pw(passwordEncoder.encode("1111"))
                .email("email@example.com")
                .name("이재혁")
                .address("경기도 수원시")
                .build();

        member.addRole(MemberRole.USER);

        memberRepository.save(member);
        return member;
    }

}
