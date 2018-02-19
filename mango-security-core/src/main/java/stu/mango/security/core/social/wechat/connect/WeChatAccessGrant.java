package stu.mango.security.core.social.wechat.connect;

import org.springframework.social.oauth2.AccessGrant;

/**
 * 微信的access_token信息。与标准OAuth2协议不同，微信在获取access_token时会同时返回openId,并没有单独的通过accessToke换取openId的服务
 *
 * 所以在这里继承了标准AccessGrant，添加了openId字段，作为对微信access_token信息的封装。
 *
 */
public class WeChatAccessGrant extends AccessGrant {

    private static final long serialVersionUID = -1151923117682204833L;
    private String openId;

    WeChatAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        super(accessToken, scope, refreshToken, expiresIn);
    }

    String getOpenId() {
        return openId;
    }

    void setOpenId(String openId) {
        this.openId = openId;
    }
}
