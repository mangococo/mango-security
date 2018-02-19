package stu.mango.security.core.social.wechat.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import stu.mango.security.core.social.wechat.api.WeChatApi;
import stu.mango.security.core.social.wechat.api.WeChatUserInfo;

public class WeChatAdapter implements ApiAdapter<WeChatApi> {

    private String openId;

    WeChatAdapter() {
    }

    @Override
    public boolean test(WeChatApi api) {
        return true;
    }

    @Override
    public void setConnectionValues(WeChatApi api, ConnectionValues values) {
            WeChatUserInfo userInfo = api.getUserInfo(openId);

            values.setDisplayName(userInfo.getNickname());
            values.setImageUrl(userInfo.getHeadimgurl());
            values.setProviderUserId(userInfo.getOpenid());
            values.setProfileUrl(null); // 个人主页
    }

    @Override
    public UserProfile fetchUserProfile(WeChatApi api) {
        return null;
    }

    @Override
    public void updateStatus(WeChatApi api, String message) {

    }

    void setOpenId(String openId) {
        this.openId = openId;
    }
}
