package stu.mango.security.core.social.qq.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import stu.mango.security.core.social.qq.api.QQApi;

public class QQConnectionFactory extends OAuth2ConnectionFactory<QQApi> {
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
