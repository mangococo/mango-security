package stu.mango.security.core.social.wechat.connect;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * 完成微信的OAuth2认证流程的模板类。
 *
 * 国内厂商实现的OAuth2每个都不同, spring默认提供的OAuth2Template适应不了，只能针对每个厂商自己微调。
 */
public class WeChatOAuth2Template extends OAuth2Template {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String clientId;

    private String clientSecret;

    private String accessTokenUrl;

    private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";

    /**
     * 由于从QQ获取Authorization Code时必须带上 client_id,client_secret参数，
     * 选哟设置useParametersForClientAuthentication为true才可以
     */
    WeChatOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.accessTokenUrl = accessTokenUrl;

        logger.info("APP_ID:" + clientId);
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 由于微信的第三方登录未完全按 OAuth2协议 实现，所以需要重新对其请求的url进行拼接
     */
    @Override
    public AccessGrant exchangeForAccess(String authorizationCode, String redirectUri, MultiValueMap<String, String> additionalParameters) {
        String accessTokenUrlBuilder = accessTokenUrl +
                "?appid=" + clientId +
                "&secret=" + clientSecret +
                "&code=" + authorizationCode +
                "&grant_type=authorization_code";

        return getAccessToken(accessTokenUrlBuilder);
    }

    @SuppressWarnings("unchecked")
    private AccessGrant getAccessToken(String accessTokenRequestUrl) {

        logger.info("请求access_token:["  + accessTokenRequestUrl + "]");

        String response = getRestTemplate().getForObject(accessTokenRequestUrl, String.class);

        logger.info("获取的响应： [" + response + "]");

        Map<String, Object> result = null;
        try {
             result = new ObjectMapper().readValue(response, Map.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotBlank(MapUtils.getString(result, "errcode"))) {
            String errcode = MapUtils.getString(result, "errcode");
            String errmsg = MapUtils.getString(result, "errmsg");
            throw new RuntimeException("获取access token失败, errcode:"+errcode+", errmsg:"+errmsg);
        }

        WeChatAccessGrant accessToken = new WeChatAccessGrant(
                MapUtils.getString(result, "access_token"),
                MapUtils.getString(result, "scope"),
                MapUtils.getString(result, "refresh_token"),
                MapUtils.getLong(result, "expires_in"));

        accessToken.setOpenId(MapUtils.getString(result, "openid"));

        return accessToken;
    }

    @Override
    public AccessGrant refreshAccess(String refreshToken, MultiValueMap<String, String> additionalParameters) {
        String refreshTokenUrl = REFRESH_TOKEN_URL +
                "?appid=" + clientId +
                "&grant_type=refresh_token" +
                "&refresh_token=" + refreshToken;

        return getAccessToken(refreshTokenUrl);
    }

    /**
     * (因为QQ携带授权码返回的格式为text/html)
     *
     * 为默认OAuth2Template添加处理text/html的能力
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate =  super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return restTemplate;
    }
}
