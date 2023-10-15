package com.forcat.forcat.security;

import com.forcat.forcat.entity.Member;
import com.forcat.forcat.entity.MemberRole;
import com.forcat.forcat.repository.MemberRepository;
import com.forcat.forcat.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override//OAuth2User 로드, 클라이언트 등록 정보, 사용자 인등 코드 등을 가져옵니다.
    public OAuth2User loadUser (OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info ("==========사용자 요청....");
        log.info (userRequest);
        log.info ("==========oauth2 유저.....................................");
        ClientRegistration clientRegistration = userRequest.getClientRegistration ();//클라이언트 등록 정보를 가져옴
        String clientName = clientRegistration.getClientName ();//클라이언트 이름을 가져온다. 구글, 네이버 등을 구분하는데 사용
        log.info ("NAME: " + clientName);
        OAuth2User oAuth2User = super.loadUser (userRequest);//사용자 소셜 로그인 정보를 가져옴
        Map<String, Object> paramMap = oAuth2User.getAttributes ();//사용자 속성 추출
        String email = null;//이메일 주소 초기화
        switch (clientName) {//클라이언트 이름 기반으로 소셜 로그인 이메일 주소 추출
            case "kakao":
                email = getKakaoEmail (paramMap);
                break;
            case "google":
                email = getGoogleEmail (paramMap);
                break;
        }
        log.info ("===============================");
        log.info ("이메일" + email);
        log.info ("===============================");
        return generateDTO (email, paramMap);//메서드를 호출하여 사용자 정보를 MemberSecurityDTO 객체로 변환하고 반환
    }

    private MemberSecurityDTO generateDTO (String email, Map<String, Object> params) {//generateDTO로 소셜 로그인 사용자와 일반 사용자를 모두 처리
        Optional<Member> result = memberRepository.findByEmail (email);//DB 이메일을 검색
        if (result.isEmpty ()) {//데이터베이스에 해당 이메일을 사용자가 없다면
            Member member = Member.builder ()//회원 추가 -- mid는 이메일 주소/ 패스워드는 1111
                    .mid (email).mpw (passwordEncoder.encode ("1111")).email (email).social (true).build ();
            member.addRole (MemberRole.USER);
            memberRepository.save (member);//회원 정보를 저장
            //MemberSecurityDTO 구성 및 반환
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO (email, "1111", email, false, true, List.of (new SimpleGrantedAuthority ("ROLE_USER")));
            memberSecurityDTO.setProps (params);//setProps 메서드를 호출하여 사용자의 추가 정보(params)를 설정
            return memberSecurityDTO;
        } else {////데이터베이스에 해당 이메일을 사용자가 있다면
            Member member = result.get ();
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO (//소셜 로그인 사용자로 설정
                    member.getMid (), member.getMpw (), member.getEmail (), member.isDel (), member.isSocial (), member.getRoleSet ().stream ().map (memberRole -> new SimpleGrantedAuthority ("ROLE_" + memberRole.name ())).collect (Collectors.toList ()));
            return memberSecurityDTO;
        }//MemberSecurityDTO는 Spring Security의 인증 및 권한 확인에 사용
    }

    private String getKakaoEmail (Map<String, Object> paramMap) {//Kakao 소셜 로그인에서 사용자의 이메일 주소를 추출
        log.info ("KAKAO-----------------------------------------");
        Object value = paramMap.get ("kakao_account");//매개변수에서 "kakao_account" 키를 사용하여 Kakao 계정 정보를 가져옴
        log.info (value);
        LinkedHashMap accountMap = (LinkedHashMap) value;//value를 LinkedHashMap 형태로 형변환하여 accountMap 변수에 저장
        String email = (String) accountMap.get ("email");//accountMap에서 "email" 키를 사용하여 사용자의 이메일 주소를 추출, "email" 키로 반환
        log.info ("email..." + email);
        return email;
    }

    private String getGoogleEmail (Map<String, Object> paramMap) {
        log.info ("GOOGLE-----------------------------------------");
        String email = (String) paramMap.get ("email");
        log.info ("email..." + email);
        return email;
    }
}