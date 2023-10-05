package com.forcat.forcat.entity;

import com.forcat.forcat.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
// 테스트 환경에서 사용할 property 소스 지정
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER")
    // Spring Security에 설정한 인증 정보를 제공
    public void auditingTest() {
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getMember_id())
                                           .orElseThrow(EntityNotFoundException::new);

        System.out.println("register Time : " + member.getRegTime());
        System.out.println("update Time : " + member.getUpdateTime());
        System.out.println("create Member : " + member.getCreatedBy());
        System.out.println("modify Member : " + member.getModifiedBy());

    }

}
