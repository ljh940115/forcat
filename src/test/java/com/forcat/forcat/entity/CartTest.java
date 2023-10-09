package com.forcat.forcat.entity;

import com.forcat.forcat.dto.member.MemberJoinDTO;
import com.forcat.forcat.entity.Cart;
import com.forcat.forcat.entity.Member;
import com.forcat.forcat.repository.CartRepository;
import com.forcat.forcat.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static com.forcat.forcat.entity.Member.createMember;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class CartTest {

    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;

    private Member createMember() {
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setMid("ggggg");
        memberJoinDTO.setName("John Doe");
        memberJoinDTO.setEmail("john@example.com");
        memberJoinDTO.setAddress("123 Main St");
        memberJoinDTO.setMpw("password123");
        return Member.createMember(memberJoinDTO, passwordEncoder);
    }

    @Test
    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
    public void findCartAndMemberTest() {
        Member member = createMember(); // Member 엔티티를 생성하여 member에 저장
        memberRepository.save(member); // 엔티티를 memberRepository에 저장

        Cart cart = new Cart(); // Cart 엔티티 생성
        cart.setMember(member); // Cart 엔티티에 member 엔티티 저장
        cartRepository.save(cart); // 엔티티를 cartRepository에 저장

        em.flush(); // 영속성 컨텍스트에 저장한 데이터를 데이터베이스에 반영
        em.clear(); // 영속성 컨텍스트 초기화

        Cart savedCart = cartRepository.findById(cart.getId()) // ID를 이용하여 cart 엔티티 조회
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(savedCart.getMember().getMid(), member.getMid());
        // assertEquals ->  두 객체의 값이 같은지?
        // 조회하여 찾아온 cart엔티티에 매핑된 Member의 id와 기존 member 엔티티의 id가 같은지 조사
        System.out.println("Cart Id: " + savedCart.getMember().getMid());
        System.out.println("Member Id: " + member.getMid());
    }
}
