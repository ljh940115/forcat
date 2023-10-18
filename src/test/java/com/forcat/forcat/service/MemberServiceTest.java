package com.forcat.forcat.service;

import com.forcat.forcat.dto.member.MemberJoinDTO;
import com.forcat.forcat.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource (locations = "classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember () {
        MemberJoinDTO memberFormDto = new MemberJoinDTO ();
        memberFormDto.setEmail ("test@email.com");
        memberFormDto.setName ("홍길동");
        memberFormDto.setAddress ("서울시 마포구 합정동");
        memberFormDto.setMpw (passwordEncoder.encode ("1111"));
        return Member.createMember (memberFormDto, passwordEncoder);
    }

    @Test
    public void testMemberRegistration () throws
                                          MemberService.MidExistException {
        MemberJoinDTO memberFormDto = new MemberJoinDTO ();
        memberFormDto.setEmail ("test@email.com");
        memberFormDto.setName ("홍길동");
        memberFormDto.setAddress ("서울시 마포구 합정동");
        memberFormDto.setMpw (passwordEncoder.encode ("1111"));
        // 회원 가입 서비스 호출
        memberService.join (memberFormDto);
        /*// 회원 정보를 데이터베이스에서 다시 가져와서 검증
        Member savedMember = memberService.findMemberByEmail(memberFormDto.getEmail());
        assertNotNull(savedMember);
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        // 기타 필요한 검증 작업 수행*/
    }

    /*@Test
    @DisplayName ("중복 회원 가입 테스트")
    public void saveDuplicateMemberTest () {
        Member member1 = createMember ();
        Member member2 = createMember ();
        memberService.saveMember (member1);
        Throwable e = assertThrows (IllegalStateException.class, () -> {
            memberService.saveMember (member2);
        });
        assertEquals ("이미 가입된 회원입니다.", e.getMessage ());
    }*/
}