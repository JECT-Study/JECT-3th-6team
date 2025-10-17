package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("dev") // dev 프로파일에서만 활성화
public class SwaggerConfig {


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JECT 3기 6팀 API")
                        .description("팝업스토어 예약 및 대기 시스템 API 문서")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("JECT 3기 6팀")
                                .email("contact@example.com")))
                .servers(List.of(
                        new Server().url("https://dev.api.spotit.co.kr").description("개발 서버")
                ));
    }
}
