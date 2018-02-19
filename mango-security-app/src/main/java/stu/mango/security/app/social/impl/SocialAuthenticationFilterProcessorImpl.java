package stu.mango.security.app.social.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.stereotype.Component;
import stu.mango.security.core.social.SocialAuthenticationFilterProcessor;

/**
 *
 * 社交登录时，在app环境下，spring默认的successHandler为页面跳转，需将重新设置社交验证过滤器的成功处理器
 */
@Component
public class SocialAuthenticationFilterProcessorImpl implements SocialAuthenticationFilterProcessor {

    @Autowired
    private AuthenticationSuccessHandler mangoAuthenticationSuccessHandler;

    @Override
    public void process(SocialAuthenticationFilter filter) {
        filter.setAuthenticationSuccessHandler(mangoAuthenticationSuccessHandler);
    }
}
