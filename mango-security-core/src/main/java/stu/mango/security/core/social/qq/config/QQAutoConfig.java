package stu.mango.security.core.social.qq.config;

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
import stu.mango.security.core.social.MangoConnectView;
import stu.mango.security.core.social.qq.connect.QQConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix = "mango.security.social.qq", name = "app-id") // 当配置了 mango.security.social.qq.app-id 时才该类才生效
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final SecurityProperties securityProperties;

    @Autowired
    public QQAutoConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        logger.info("QQ 登录配置生效");

        QQProperties properties = securityProperties.getSocial().getQq();
        return new QQConnectionFactory(properties.getProviderId(), properties.getAppId(), properties.getAppSecret());
    }


    @Bean({"connect/qqConnected", "connect/qqConnect"})
    @ConditionalOnMissingBean(name = "qqConnectedView")
    public View qqConnectedView() {
        return new MangoConnectView();
    }
}
