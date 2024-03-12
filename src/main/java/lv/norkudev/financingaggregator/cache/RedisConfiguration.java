package lv.norkudev.financingaggregator.cache;

import lv.norkudev.financingaggregator.model.ApplicationOffer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration();
        redisStandaloneConfig.setHostName(redisHost);
        redisStandaloneConfig.setPort(redisPort);
        return new LettuceConnectionFactory(redisStandaloneConfig);
    }

    @Bean
    public ReactiveRedisOperations<String, ApplicationOffer> redisOperations(LettuceConnectionFactory connectionFactory) {
        var jacksonSerializer = new Jackson2JsonRedisSerializer<>(ApplicationOffer.class);
        RedisSerializationContext<String, ApplicationOffer> serializationContext = RedisSerializationContext
                .<String, ApplicationOffer>newSerializationContext(new StringRedisSerializer())
                .key(new StringRedisSerializer())
                .value(jacksonSerializer)
                .hashKey(jacksonSerializer)
                .hashValue(jacksonSerializer)
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }

}
