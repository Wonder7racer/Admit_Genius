package com.admitgenius.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:openai.properties")  // 指定自定义配置文件
@ConfigurationProperties(prefix = "openai")  // 属性前缀
public class OpenAIConfig {
    private ApiConfig api = new ApiConfig();
    private String proxyUrl;    // 对应 openai.proxy.url

    @Getter @Setter
    public static class ApiConfig {
        private String key;
        private String proxyUrl; 

    }
}