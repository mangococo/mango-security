package stu.mango.security.core.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;

public class QQImpl extends AbstractOAuth2ApiBinding implements QQApi {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取用户的ID，与QQ号码一一对应。
     *
     * @access_token 令牌
     */
    private static final String URL_GET_OPEN_ID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * 获取用户信息
     *
     * @access_token 令牌，由AbstractOAuth2ApiBinding完成参数填充
     * @oauth_consumer_key 申请QQ登录成功后，分配给应用的 appId
     * @openid 用户的ID，与QQ号码一一对应。
     */
    private static final String URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appId;

    private String openId;

    private ObjectMapper objectMapper;

    public QQImpl(String accessToken, String appId) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;
        this.objectMapper = new ObjectMapper();

        String url = String.format(URL_GET_OPEN_ID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);

        logger.info("获取open_id:[" + result + "]");

        this.openId = StringUtils.substringBetween(result, "\"openid:\":\"", "\"}");
    }

    @Override
    public QQUserInfo getUserInfo() {
        String url = String.format(URL_GET_USER_INFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);

        logger.info("获取用户信息:[" + result + "]");

        try {
            QQUserInfo userInfo = objectMapper.readValue(result, QQUserInfo.class);
            userInfo.setOpenId(openId);

            return userInfo;
        } catch (IOException e) {
            throw  new RuntimeException("获取用户信息失败");
        }
    }
}
