package stu.mango.security.core.social.wechat.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class WeChatImpl extends AbstractOAuth2ApiBinding implements WeChatApi {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取用户信息
     *
     * @access_token 令牌，由AbstractOAuth2ApiBinding完成参数填充
     * @openid 用户的ID，与QQ号码一一对应。
     */
    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=%s";

    private ObjectMapper objectMapper;

    public WeChatImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 默认注册的StringHttpMessageConverter(converters的第一个元素)字符集编码为ISO-8859-1, 而微信返回的是UTF-8
     */
    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("utf-8")));

        return messageConverters;
    }

    @Override
    public WeChatUserInfo getUserInfo(String openId) {
        String url = String.format(URL_GET_USER_INFO, openId);
        String result = getRestTemplate().getForObject(url, String.class);
        if (StringUtils.contains(result, "errcode")) {
            return null;
        }

        logger.info("获取用户信息:[" + result + "]");

        try {
            WeChatUserInfo userInfo = objectMapper.readValue(result, WeChatUserInfo.class);
            userInfo.setOpenid(openId);

            return userInfo;
        } catch (IOException e) {
            throw  new RuntimeException("获取用户信息失败");
        }
    }
}
