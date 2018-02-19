package stu.mango.security.core.social.wechat.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import stu.mango.security.core.social.wechat.api.WeChatApi;
import stu.mango.security.core.social.wechat.api.WeChatImpl;

public class WeChatServiceProvider extends AbstractOAuth2ServiceProvider<WeChatApi> {

    /**
     * 引导用户授权的 URL
     */
    private static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/qrconnect";

    /**
     * 根据用户授权后的授权码申请令牌的 URL
     */
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    WeChatServiceProvider(String appId, String appSecret) {
        super(new WeChatOAuth2Template(appId, appSecret, AUTHORIZE_URL, ACCESS_TOKEN_URL));
    }

    @Override
    public WeChatApi getApi(String accessToken) {
        return new WeChatImpl(accessToken);
    }
}
