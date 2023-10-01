package org.zerock.b01.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//SwaggerConfig는 API 문서를 자동으로 생성하고 테스트할 수 있는 도구
@Configuration//환경 설정 파일 명시
public class SwaggerConfig {

    @Bean//Bean 정의
    public Docket api() {//Docket이라는 스웩 설정 객체 반환 메서드
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)//기본 응답 메세지 사용하지 않는다. 사용자 설정 가능
                .select()//문서화할 부분 선택
                .apis(RequestHandlerSelectors.basePackage("com.forcat.forcat.controller"))
                .paths(PathSelectors.any())//모든 경로를 문서화 대상으로 지정
                .build()//빌드하여 설정 완료
                .apiInfo(apiInfo());//api 정보 설정
    }

    private ApiInfo apiInfo() {//api 문서에 표시될 정보 설정 메서드
        return new ApiInfoBuilder()
                .title("Boot 01 Project Swagger")
                .build();
    }
}
