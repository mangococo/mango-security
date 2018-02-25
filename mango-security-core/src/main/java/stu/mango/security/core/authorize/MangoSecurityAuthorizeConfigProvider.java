package stu.mango.security.core.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;
import stu.mango.security.core.properties.SecurityConstants;

/**
 * 安全系统模块所需要的基本授权配置
 */
@Component
public class MangoSecurityAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests) {
        authorizeRequests.antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL, // 未进行身份验证时跳转的URL
                    SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, // 短信验证码登录的URL
                    SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*" // 验证码获取及校验URL
                ).permitAll(); // 这些请求无需需要身份认证
    }
}
