package stu.mango.security.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;
import stu.mango.security.app.social.openid.OpenIdAuthenticationSecurityConfig;
import stu.mango.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import stu.mango.security.core.authorize.AuthorizeConfigManager;
import stu.mango.security.core.properties.SecurityConstants;

import javax.sql.DataSource;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final DataSource dataSource;

    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    private final SpringSocialConfigurer mangoSpringSocialConfigurer;

    private final AuthenticationSuccessHandler mangoAuthenticationSuccessHandler;

    private final AuthenticationFailureHandler mangoAuthenticationFailureHandler;

    private final OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

    private final AuthorizeConfigManager authorizeConfigManager;

    @Autowired
    public ResourceServerConfiguration(DataSource dataSource,
                                       SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig,
                                       SpringSocialConfigurer mangoSpringSocialConfigurer,
                                       AuthenticationSuccessHandler mangoAuthenticationSuccessHandler,
                                       AuthenticationFailureHandler mangoAuthenticationFailureHandler,
                                       OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig,
                                       AuthorizeConfigManager authorizeConfigManager) {
        this.dataSource = dataSource;
        this.smsCodeAuthenticationSecurityConfig = smsCodeAuthenticationSecurityConfig;
        this.mangoSpringSocialConfigurer = mangoSpringSocialConfigurer;
        this.mangoAuthenticationSuccessHandler = mangoAuthenticationSuccessHandler;
        this.mangoAuthenticationFailureHandler = mangoAuthenticationFailureHandler;
        this.openIdAuthenticationSecurityConfig = openIdAuthenticationSecurityConfig;
        this.authorizeConfigManager = authorizeConfigManager;
    }


    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        /*
        JdbcTokenRepositoryImpl 中自定义了Token存储表的操作语句，可使用启动时自动创建表
         */
        tokenRepository.setCreateTableOnStartup(false);

        return tokenRepository;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 表单登录
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(mangoAuthenticationSuccessHandler)
                .failureHandler(mangoAuthenticationFailureHandler)
                .and()

                .apply(smsCodeAuthenticationSecurityConfig).and()
                .apply(mangoSpringSocialConfigurer).and()
                .apply(openIdAuthenticationSecurityConfig).and()

                .csrf().disable() // 跨站伪造防护功能
        ;

        authorizeConfigManager.config(http.authorizeRequests());
    }
}
