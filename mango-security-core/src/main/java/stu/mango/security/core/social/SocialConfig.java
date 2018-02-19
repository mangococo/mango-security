package stu.mango.security.core.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;
import stu.mango.security.core.properties.SecurityProperties;
import stu.mango.security.core.properties.SocialProperties;

import javax.sql.DataSource;

@EnableSocial
@Configuration
public class SocialConfig extends SocialConfigurerAdapter {

    private final DataSource dataSource;

    private final SecurityProperties securityProperties;
    /**
     * 并不要求所有应用都提供该Bean（提供该组件，第三方登录后查询userId时由系统默认注册）
     */
    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    @Autowired(required = false)
    private SocialAuthenticationFilterProcessor socialAuthenticationFilterProcessor;

    @Autowired
    public SocialConfig(DataSource dataSource, SecurityProperties securityProperties) {
        this.dataSource = dataSource;
        this.securityProperties = securityProperties;
    }


    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        // 第三个参数是数据存入数据库时的加密方式
        JdbcUsersConnectionRepository repository =  new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        repository.setTablePrefix("mango_");
        repository.setConnectionSignUp(connectionSignUp);

        return repository;
    }

    @Bean
    public SpringSocialConfigurer mangoSpringSocialConfigurer() {
        SocialProperties social =  securityProperties.getSocial();
        MangoSpringSocialConfigurer configurer = new MangoSpringSocialConfigurer(social.getFilterProcessUrl(),
                social.getFilterFailureUrl());
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        configurer.setSocialAuthenticationFilterProcessor(socialAuthenticationFilterProcessor);

        return configurer;
    }

    /**
     *
     * @param connectionFactoryLocator 用于定位ConnectionFactory
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator, getUsersConnectionRepository(connectionFactoryLocator));
    }
}
