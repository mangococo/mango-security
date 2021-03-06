package stu.mango.security.browser.authorize;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;
import stu.mango.security.core.authorize.AuthorizeConfigProvider;
import stu.mango.security.core.properties.BrowserProperties;
import stu.mango.security.core.properties.SecurityProperties;

@Component
@Order(1)
public class MangoBrowserAuthorizeConfigProvider implements AuthorizeConfigProvider {

    private final SecurityProperties securityProperties;

    @Autowired
    public MangoBrowserAuthorizeConfigProvider(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        BrowserProperties browser = securityProperties.getBrowser();

        authorizeRequests
                .antMatchers(browser.getSignInUrl(),
                        browser.getSignUpUrl(),
                        securityProperties.getBrowser().getSession().getSessionInvalidUrl()
                ).access("permitAll()"); // 除此之外需要身份认证

        if (StringUtils.isNotBlank(browser.getSignOutUrl())) {
            authorizeRequests.antMatchers(browser.getSignOutUrl()).permitAll();
        }

        return false;
    }
}
