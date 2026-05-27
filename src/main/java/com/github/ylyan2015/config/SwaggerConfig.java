package com.github.ylyan2015.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger配置类
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Cloud Demo API文档")
                        .version("1.0.0")
                        .description("基于Spring Cloud的微服务用户权限管理系统API接口文档")
                        .contact(new Contact()
                                .name("ylyan2015")
                                .email("ylyan2015@example.com")
                                .url("https://github.com/ylyan2015/spring-cloud-demo"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
