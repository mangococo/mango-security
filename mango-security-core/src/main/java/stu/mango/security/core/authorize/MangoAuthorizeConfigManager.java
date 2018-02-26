package stu.mango.security.core.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 收集系统内的所有授权配置，并遍历地将其加到系统安全模块中。
 */
@Component
public class MangoAuthorizeConfigManager implements AuthorizeConfigManager {

    private final List<AuthorizeConfigProvider> authorizeConfigProviders;

    @Autowired
    public MangoAuthorizeConfigManager(List<AuthorizeConfigProvider> authorizeConfigProviders) {
        this .authorizeConfigProviders = authorizeConfigProviders;
    }

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        authorizeConfigProviders.forEach(provider -> provider.config(authorizeRequests));
        // authorizeRequests.anyRequest().authenticated();
    }
}
