package stu.mango.security.app.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;
import stu.mango.security.core.authorize.AuthorizeConfigProvider;
import stu.mango.security.core.properties.SecurityConstants;

@Component
public class MangoAppAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        authorizeRequests.antMatchers(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_OPEN_ID,
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_SIGN_UP_URL_APP
        ).permitAll();
    }
}
