package stu.mango.security;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;
import stu.mango.security.core.authorize.AuthorizeConfigProvider;

@Component
@Order // 由于anyRequest 需要在处理完特殊请求后进行配置，我们需要自定义security对该配置的加载顺序（最后），@Order 注解声明顺序，其默认值为 Integer.MAX_VALUE
public class DemoAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        authorizeRequests.anyRequest().access("@rbacService.hasPermission(request, authentication)");
    }
}
