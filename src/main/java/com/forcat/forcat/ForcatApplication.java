package com.forcat.forcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication//시큐리티 비활성 : (exclude = SecurityAutoConfiguration.class)
@EnableJpaAuditing//BaseEntity에 감시 기능을 사용하기 위해 감시 기능 활성화 명시
public class ForcatApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForcatApplication.class, args);
    }
}
