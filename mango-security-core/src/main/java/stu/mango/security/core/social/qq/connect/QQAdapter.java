package stu.mango.security.core.social.qq.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import stu.mango.security.core.social.qq.api.QQApi;
import stu.mango.security.core.social.qq.api.QQUserInfo;

public class QQAdapter implements ApiAdapter<QQApi> {
    @Override
    public boolean test(QQApi api) {
        return true;
    }

    @Override
    public void setConnectionValues(QQApi api, ConnectionValues values) {
            QQUserInfo userInfo = api.getUserInfo();

            values.setDisplayName(userInfo.getNickname());
            values.setImageUrl(userInfo.getFigureurl_qq_1());
            values.setProviderUserId(userInfo.getOpenId());
            values.setProfileUrl(null); // 个人主页
    }

    @Override
    public UserProfile fetchUserProfile(QQApi api) {
        return null;
    }

    @Override
    public void updateStatus(QQApi api, String message) {

    }
}
