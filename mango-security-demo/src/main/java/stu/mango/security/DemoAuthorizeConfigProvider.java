package stu.mango.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;
import stu.mango.security.core.authorize.AuthorizeConfigProvider;

@Component
public class DemoAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        authorizeRequests.antMatchers("/user/*").hasRole("ADMIN")
                .antMatchers("/register").permitAll();
    }
}
