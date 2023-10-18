package com.forcat.forcat.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {//사용자 인증 클래스

    @Override
    public void commence (HttpServletRequest request,//사용자의 요청에 대한 정보를 나타냄
                          HttpServletResponse response,//사용자에 대한 응답을 생성
                          AuthenticationException authException)//인증 예외에 관한 정보
            throws
            IOException,//스트림, 파일, 디렉터리 사용 정보 접근 예외 처리
            ServletException {//서버 응답 예외 처리
        if ("XMLHttpRequest".equals (request.getHeader ("x-requested-with"))) {//XML헤더가 있는 경우 Ajax 요청인지 확인
            response.sendError (HttpServletResponse.SC_UNAUTHORIZED, "허용되지 않음");//사용자 인증되지 않음을 알림
        } else {
            response.sendRedirect ("/member/login");
        }
    }
}