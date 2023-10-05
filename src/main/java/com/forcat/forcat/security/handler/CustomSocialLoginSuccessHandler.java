package com.forcat.forcat.security.handler;

import com.forcat.forcat.security.dto.MemberSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2//로그 출력
@RequiredArgsConstructor//해당 필드 생성자 자동 생성
//소셜 로그인 시 커스터 마이징 동작
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("==================================================");
        log.info("==========사용자 정의 로그인 성공 핸들러, 로그인 성공 대응 핸들러 ..........");
        log.info("==========로그인 사용자의 정보 객체 반환 ..........");
        log.info(authentication.getPrincipal());
        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        String encodedPw = memberSecurityDTO.getMpw();//암호화된 비밀번호 저장
        if (memberSecurityDTO.isSocial()//소셜로그인, 회원의 패스워드가 1111 확인
                && (memberSecurityDTO.getMpw().equals("1111")
                || passwordEncoder.matches("1111", memberSecurityDTO.getMpw())
        )) {
            log.info("==========비밀번호 변경 필요");
            log.info("==========회원 수정 페이지로 이동합니다.");
            response.sendRedirect("/member/mypage");
            return;
        } else { response.sendRedirect("/index");}
    }
}