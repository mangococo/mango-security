package stu.mango.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import stu.mango.security.browser.logout.MangoLogoutSuccessHandler;
import stu.mango.security.core.properties.SecurityProperties;

@Configuration
public class DemoSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new MangoLogoutSuccessHandler(securityProperties.getBrowser().getSignUpUrl());
    }
}
