package stu.mango.security.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

public class MangoSpringSocialConfigurer extends SpringSocialConfigurer {

    private String filterProcessUrl;

    private String filterFailureUrl;

    private SocialAuthenticationFilterProcessor socialAuthenticationFilterProcessor;


    MangoSpringSocialConfigurer(String filterProcessUrl, String filterFailureUrl) {
        this.filterProcessUrl = filterProcessUrl;
        this.filterFailureUrl = filterFailureUrl;
    }

    /**
     *
     * @param object SocialAuthenticationFilter
     *
     * 社交登录时，Spring默认的成功处理器在浏览器环境下可已使用（当用户认证完成后跳转页面至第三方应用）
     *               但是，在app环境先，spring默认的successHandler就无法满足需求，需特别处理
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessUrl);
        filter.setDefaultFailureUrl(filterFailureUrl);
        if (socialAuthenticationFilterProcessor != null) {
            socialAuthenticationFilterProcessor.process(filter);
        }

        return (T) filter;
    }

    public void setSocialAuthenticationFilterProcessor(SocialAuthenticationFilterProcessor socialAuthenticationFilterProcessor) {
        this.socialAuthenticationFilterProcessor = socialAuthenticationFilterProcessor;
    }
}
