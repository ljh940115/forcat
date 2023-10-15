package com.forcat.forcat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc//Spring Web MVC 활성화
public class CustomServletConfig implements WebMvcConfigurer {//Spring Web MVC 구성 커스터마이징

    @Value ("${uploadPath}")// import 시에 springframework으로 시작하는 Value
    String uploadPath;//파일을 업로드하는 경로

    @Override//스프링 웹 정적 리소스 위치 설정
    public void addResourceHandlers (ResourceHandlerRegistry registry) {//정적 리소스 핸들러 등록
        registry.addResourceHandler ("/js/**")//URL 패턴 설정, 요청이 오면 자바 스크립트 파일 제공
                .addResourceLocations ("classpath:/static/js/", "classpath:/static/js/board/");
        registry.addResourceHandler ("/fonts/**")//URL 패턴 설정, 요청이 오면 폰트 파일 제공
                .addResourceLocations ("classpath:/static/fonts/");
        registry.addResourceHandler ("/css/**")//URL 패턴 설정, 요청이 오면 css 파일 제공
                .addResourceLocations ("classpath:/static/css/");
        registry.addResourceHandler ("/images/**").//URL 패턴 설정, 요청이 오면 어셋 파일 제공
                addResourceLocations ("classpath:/static/images/");
    }
}
