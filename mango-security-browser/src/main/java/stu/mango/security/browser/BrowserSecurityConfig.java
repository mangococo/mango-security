package stu.mango.security.browser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;
import stu.mango.security.core.authentication.AbstractChannelSecurityConfig;
import stu.mango.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import stu.mango.security.core.authorize.AuthorizeConfigManager;
import stu.mango.security.core.properties.SecurityProperties;
import stu.mango.security.core.properties.SessionProperties;
import stu.mango.security.core.validate.code.ValidateCodeAuthenticationConfig;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {
    private final SecurityProperties securityProperties;

    private final DataSource dataSource;

    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    private final SpringSocialConfigurer mangoSpringSocialConfigurer;

    private final ValidateCodeAuthenticationConfig validateCodeAuthenticationConfig;


    private final InvalidSessionStrategy invalidSessionStrategy;

    private final SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    private final LogoutSuccessHandler logoutSuccessHandler;

    private final AuthorizeConfigManager authorizeConfigManager;

    private final UserDetailsService userDetailsService;

    /**
     * @param securityProperties 应用配置
     * @param dataSource 数据库源，由使用者提供
     * @param mangoSpringSocialConfigurer spring social 相关配置
     * @param validateCodeAuthenticationConfig 验证码校验相关配置
     * @param userDetailsService 用户查询服务
     * @param authorizeConfigManager 请求授权配置管理器
     */
    @Autowired
    public BrowserSecurityConfig(SecurityProperties securityProperties,
                                 DataSource dataSource, SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig,
                                 UserDetailsService userDetailsService,
                                 AuthenticationFailureHandler mangoAuthenticationFailureHandler,
                                 AuthenticationSuccessHandler mangoAuthenticationSuccessHandler,
                                 SpringSocialConfigurer mangoSpringSocialConfigurer,
                                 ValidateCodeAuthenticationConfig validateCodeAuthenticationConfig,
                                 InvalidSessionStrategy invalidSessionStrategy,
                                 SessionInformationExpiredStrategy sessionInformationExpiredStrategy,
                                 LogoutSuccessHandler logoutSuccessHandler, AuthorizeConfigManager authorizeConfigManager) {

        super(mangoAuthenticationFailureHandler, mangoAuthenticationSuccessHandler);
        this.securityProperties = securityProperties;
        this.dataSource = dataSource;
        this.smsCodeAuthenticationSecurityConfig = smsCodeAuthenticationSecurityConfig;
        this.userDetailsService = userDetailsService;
        this.mangoSpringSocialConfigurer = mangoSpringSocialConfigurer;
        this.validateCodeAuthenticationConfig = validateCodeAuthenticationConfig;
        this.invalidSessionStrategy = invalidSessionStrategy;
        this.sessionInformationExpiredStrategy = sessionInformationExpiredStrategy;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.authorizeConfigManager = authorizeConfigManager;
    }

    /**
     * 持久性令牌库，系统默认为数据库存储
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();

        tokenRepository.setDataSource(dataSource);

        /*
        JdbcTokenRepositoryImpl 中自定义了Token存储表的操作语句，可使用启动时自动创建表
         */
        tokenRepository.setCreateTableOnStartup(
                securityProperties.getBrowser().isCreateTokenRepositoryTableOnStartup());

        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SessionProperties session = securityProperties.getBrowser().getSession();

        applyPasswordAuthenticationConfig(http);

        /*
        将自定义的验证码验证过滤器加到spring-security-filter-chain中
        （且在UsernamePasswordAuthenticationFilter之前）
         */
        http.apply(smsCodeAuthenticationSecurityConfig).and() // 增添自定义配置（扩展）
            .apply(validateCodeAuthenticationConfig).and()
            .apply(mangoSpringSocialConfigurer).and()
            .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
            .sessionManagement()
                .invalidSessionStrategy(invalidSessionStrategy)
                .maximumSessions(session.getMaximumSessions()) //每用户，最大session数为1，同一用户后登录的session会挤掉后者的session
                .maxSessionsPreventsLogin(session.isMaxSessionsPreventsLogin()) // 当session数达到最大，阻止之后的登录
                .expiredSessionStrategy(sessionInformationExpiredStrategy) // 针对记录
                .and()
                .and()
            .logout()
                .logoutUrl("/signOut")
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID") // 用户登出时删除指定 Cookie
                .and()
            .csrf().disable() // 跨站伪造防护功能
        ;

        authorizeConfigManager.config(http.authorizeRequests());
    }

}
