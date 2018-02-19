package stu.mango.security.app.social.openid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class OpenIdAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final AuthenticationSuccessHandler mongoAuthenticationSuccessHandler;

    private final AuthenticationFailureHandler mangoAuthenticationFailureHandler;

    private final UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired
    public OpenIdAuthenticationSecurityConfig(AuthenticationFailureHandler mangoAuthenticationFailureHandler,
                                              AuthenticationSuccessHandler mongoAuthenticationSuccessHandler,
                                              UserDetailsService userDetailsService) {
        this.mangoAuthenticationFailureHandler = mangoAuthenticationFailureHandler;
        this.userDetailsService = userDetailsService;
        this.mongoAuthenticationSuccessHandler = mongoAuthenticationSuccessHandler;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        OpenIdAuthenticationFilter filter = new OpenIdAuthenticationFilter(mongoAuthenticationSuccessHandler, mangoAuthenticationFailureHandler);

        filter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        OpenIdAuthenticationProvider provider = new OpenIdAuthenticationProvider(userDetailsService, jdbcUsersConnectionRepository());

        http.authenticationProvider(provider)
                .addFilterAfter(filter, UsernamePasswordAuthenticationFilter.class);
    }

    private JdbcUsersConnectionRepository jdbcUsersConnectionRepository() {
        JdbcUsersConnectionRepository repository =  new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        repository.setTablePrefix("mango_");

        return repository;
    }
}
