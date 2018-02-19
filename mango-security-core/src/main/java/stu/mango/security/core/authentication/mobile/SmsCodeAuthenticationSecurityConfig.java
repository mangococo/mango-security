package stu.mango.security.core.authentication.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final AuthenticationSuccessHandler mongoAuthenticationSuccessHandler;

    private final AuthenticationFailureHandler mangoAuthenticationFailureHandler;

    private final UserDetailsService userDetailsService;

    @Autowired
    public SmsCodeAuthenticationSecurityConfig(AuthenticationFailureHandler mangoAuthenticationFailureHandler, UserDetailsService userDetailsService, AuthenticationSuccessHandler mongoAuthenticationSuccessHandler) {
        this.mangoAuthenticationFailureHandler = mangoAuthenticationFailureHandler;
        this.userDetailsService = userDetailsService;
        this.mongoAuthenticationSuccessHandler = mongoAuthenticationSuccessHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter filter = new SmsCodeAuthenticationFilter(mongoAuthenticationSuccessHandler, mangoAuthenticationFailureHandler);
        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        SmsCodeAuthenticationProvider provider = new SmsCodeAuthenticationProvider(userDetailsService);

        http.authenticationProvider(provider)
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
