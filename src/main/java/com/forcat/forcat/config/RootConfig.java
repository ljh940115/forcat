package com.forcat.forcat.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper() {//getMapper() 메서드는 ModelMapper 객체를 반환
        ModelMapper modelMapper = new ModelMapper();//ModelMapper 객체 생성
        modelMapper.getConfiguration()//ModelMapper의 구성(Configuration)을 가져옴, 이를 통해 ModelMapper의 동작 방식을 구성
                .setFieldMatchingEnabled(true)//필드 매칭 활성화를 설정
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)//필드 접근 레벨 : PRIVATE
                .setMatchingStrategy(MatchingStrategies.LOOSE);//필드 매칭 전략 : 유사하면 자동 매핑
        return modelMapper;//구성된 매핑 객체를 반환
    }
}
