package design.kfu.tgintegrator.config;

import design.kfu.tgintegrator.domain.model.Message;
import design.kfu.tgintegrator.util.CustomGenericJackson2JsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;

/**
 * @author Daniyar Zakiev
 */
@Configuration
@EnableRedisRepositories(basePackageClasses = {Message.class})
public class RedisConfig {

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHostName);
        configuration.setPort(redisPort);
        configuration.setPassword(password);
        return new LettuceConnectionFactory(configuration);
    }

/*
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(lettuceConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

/*
    */
/**
     * Сериализует класс в json добавляя атрибут @class, указывающий на конкретный класс
     * @return
     *//*

    @Bean(name = "springSessionDefaultRedisSerializer")
    public GenericJackson2JsonRedisSerializer getGenericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
*/

    @Bean
    public RedisTemplate<String, Object> getRedisTemplate(
    ) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
       /* redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());*/

        redisTemplate.setDefaultSerializer(new CustomGenericJackson2JsonRedisSerializer());
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        return redisTemplate;
    }

}
