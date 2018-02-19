package stu.mango.security.core.social.wechat.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;
import stu.mango.security.core.social.wechat.api.WeChatApi;

public class WeChatConnectionFactory extends OAuth2ConnectionFactory<WeChatApi> {
    public WeChatConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeChatServiceProvider(appId, appSecret), new WeChatAdapter());
    }

    /**
     * 由于微信的openid是随access_token一起返回的，所以在取得access_token后需要手动设置openid
     * @param accessGrant 申请access_token的返回数据
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof  WeChatAccessGrant) {
            return ((WeChatAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }

    private ApiAdapter<WeChatApi> getApiAdapter(String openId) {
        WeChatAdapter weChatAdapter = (WeChatAdapter) super.getApiAdapter();
        weChatAdapter.setOpenId(openId);

        return weChatAdapter;
    }

    @Override
    public Connection<WeChatApi> createConnection(ConnectionData data) {
        return new OAuth2Connection<>(data, (OAuth2ServiceProvider<WeChatApi>) getServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    @Override
    public Connection<WeChatApi> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter());
    }

    private OAuth2ServiceProvider<WeChatApi> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<WeChatApi>) getServiceProvider();
    }
}
