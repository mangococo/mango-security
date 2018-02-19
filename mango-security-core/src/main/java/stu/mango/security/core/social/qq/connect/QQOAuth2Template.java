package stu.mango.security.core.social.qq.connect;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

public class QQOAuth2Template extends OAuth2Template {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 由于从QQ获取Authorization Code时必须带上 client_id,client_secret参数，
     * 选哟设置useParametersForClientAuthentication为true才可以
     */
    QQOAuth2Template(String appId, String appSecret, String authorizeUrl, String accessTokenUrl) {
        super(appId, appSecret, authorizeUrl, accessTokenUrl);

        logger.info("APP_ID:" + appId);
        setUseParametersForClientAuthentication(true);
    }

    /**
     * 因为QQ返回access_token等信息的格式为字符串而非预期的json
     *
     * 从qq发回的字符串中解析出acc自定义解析出（"access_token","refresh_token","expires_in"）
     *
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseFromQQ = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);

        logger.info("获取access_token的响应数据:[" + responseFromQQ + "]" );
        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseFromQQ, "&");

        String accessToken = StringUtils.substringAfter(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfter(items[1], "="));
        String refreshToken = StringUtils.substringAfter(items[2], "=");


        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
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
