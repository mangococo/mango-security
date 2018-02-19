package stu.mango.security.app;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import stu.mango.security.core.properties.OAuth2ClientProperties;
import stu.mango.security.core.properties.SecurityProperties;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final TokenStore redisTokenStore;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    private OAuth2ClientProperties[] clientProperties;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public AuthorizationServerConfiguration(SecurityProperties securityProperties,
                                            AuthenticationManager authenticationManager,
                                            UserDetailsService userDetailsService,
                                            TokenStore redisTokenStore) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.clientProperties = securityProperties.getOauth2().getClients();
        this.redisTokenStore = redisTokenStore;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(redisTokenStore)
                .userDetailsService(userDetailsService);
        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancerList = new ArrayList<>();
            enhancerList.add(jwtTokenEnhancer);
            enhancerList.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancerList);

            endpoints
                    .tokenEnhancer(enhancerChain);
                    //.accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        if (ArrayUtils.isEmpty(clientProperties)) {
            logger.info("未配置第三方应用信息,APP应用将无法使用密码模式获取access_token");
            return;
        }

        InMemoryClientDetailsServiceBuilder builder = clients.inMemory(); // client信息存储于内存（应用需求是提供本应用APP或Browser通信）

        for (OAuth2ClientProperties client : clientProperties) {
            logger.info("{clientId:" + client.getClientId() +
                    ", clientSecret:" + client.getClientSecret() +
                    ", validitySeconds:" + client.getAccessTokenValiditySeconds() +
                    "}");

            builder.withClient(client.getClientId())
                    .secret(client.getClientSecret())
                    .accessTokenValiditySeconds(client.getAccessTokenValiditySeconds())
                    .authorizedGrantTypes("password", "refresh_token") // oauth2 请求模式
                    .refreshTokenValiditySeconds(259200) // 刷新令牌的有效时间
                    .scopes("all", "read", "write"); // 权限
        }
    }
}
