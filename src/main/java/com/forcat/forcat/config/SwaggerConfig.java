package com.forcat.forcat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration//환경 설정 파일 명시
public class SwaggerConfig {//SwaggerConfig는 API 문서를 자동으로 생성하고 테스트할 수 있는 도구

    @Bean
    public Docket api() {//Docket이라는 스웩 설정 객체 반환, Swagger 설정을 구성
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)//기본 응답 메세지 사용하지 않는다. 사용자 설정 가능
                .select()//문서화할 부분 선택
                .apis(RequestHandlerSelectors.basePackage("com.forcat.forcat.controller"))//API 문서화 대상 패키지를 설정
                .paths(PathSelectors.any())//문서화할 경로를 설정
                .build()//빌드하여 설정 완료
                .apiInfo(apiInfo());//api 정보 설정
    }

    private ApiInfo apiInfo() {//api 문서에 표시될 정보 설정
        return new ApiInfoBuilder()//API의 제목을 설정하고, 나머지 정보는 기본값으로 설정
                .title("Boot 01 Project Swagger")
                .build();
    }
}
