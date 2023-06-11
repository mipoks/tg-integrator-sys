package design.kfu.tgintegrator.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
@Configuration
public class FeignConfig {

    @Autowired
    private Environment environment;

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return requestTemplate -> {
//            requestTemplate.header("Content-Type", "application/json");
//        };
//    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
