package stu.mango.security.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import stu.mango.security.app.jwt.MangoJwtTokenEnhancer;
import stu.mango.security.core.properties.SecurityProperties;

@Configuration
public class TokenStoreConfiguration {

    /**
     * 当且仅当在配置文件中配置了mango.security.oauth2.storeType=redis时,该TokenStore生效
     */
    @Bean
    @ConditionalOnProperty(prefix = "mango.security.oauth2", name = "storeType", havingValue = "redis")
    public TokenStore redisTokenStore(RedisConnectionFactory connectionFactory) {
        return new RedisTokenStore(connectionFactory);
    }

    /**
     * 当在配置文件中配置了mango.security.oauth2.storeType=jwt时使用该TokenStore(当不配置时默认使用改配置)
     */
    @Configuration
    @ConditionalOnProperty(prefix = "mango.security.oauth2", name = "storeType", havingValue = "jwt", matchIfMissing = true)
    public static class JWTTokenConfig {

        private final String signingKey;

        @Autowired
        public JWTTokenConfig(SecurityProperties securityProperties) {
            signingKey = securityProperties.getOauth2().getJwtSigningKey();
        }


        @Bean
        public TokenStore jwtTokenStore() {
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter() {
            JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
            jwtAccessTokenConverter.setSigningKey(signingKey);

            return jwtAccessTokenConverter;
        }

        @Bean
        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer() {
            return new MangoJwtTokenEnhancer();
        }

    }
}
