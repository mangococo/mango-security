package stu.mango.security.core.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import stu.mango.security.core.properties.SecurityConstants;

public class AbstractChannelSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationFailureHandler mangoAuthenticationFailureHandler;

    private final AuthenticationSuccessHandler mangoAuthenticationSuccessHandler;

    @Autowired
    public AbstractChannelSecurityConfig(AuthenticationFailureHandler mangoAuthenticationFailureHandler, AuthenticationSuccessHandler mangoAuthenticationSuccessHandler) {
        this.mangoAuthenticationFailureHandler = mangoAuthenticationFailureHandler;
        this.mangoAuthenticationSuccessHandler = mangoAuthenticationSuccessHandler;
    }


    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
        http.formLogin() // 表单登录
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM) // security默认处理登陆url为"/login",自定义
                .successHandler(mangoAuthenticationSuccessHandler)
                .failureHandler(mangoAuthenticationFailureHandler);
    }
}
