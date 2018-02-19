package stu.mango.security.core.social.wechat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;
import stu.mango.security.core.properties.QQProperties;
import stu.mango.security.core.properties.SecurityProperties;
import stu.mango.security.core.properties.WeChatProperties;
import stu.mango.security.core.social.MangoConnectView;
import stu.mango.security.core.social.wechat.connect.WeChatConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix = "mango.security.social.wechat", name = "app-id") // 当配置了 mango.security.social.wechat.app-id 时才该类才生效
public class WeChatAutoConfiguration extends SocialAutoConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final SecurityProperties securityProperties;

    @Autowired
    public WeChatAutoConfiguration(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        logger.info("微信 登录配置生效");

        WeChatProperties properties = securityProperties.getSocial().getWechat();
        return new WeChatConnectionFactory(properties.getProviderId(), properties.getAppId(), properties.getAppSecret());
    }

    @Bean({"connect/wechatConnected", "connect/wechatConnect"}) // {绑定，解绑}
    @ConditionalOnMissingBean(name = "wechatConnectedView")
    public View wechatConnectedView() {
        return new MangoConnectView();
    }
}
