package stu.mango.security.app.validate.code.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import stu.mango.security.core.validate.code.ValidateCode;
import stu.mango.security.core.validate.code.ValidateCodeException;
import stu.mango.security.core.validate.code.ValidateCodeRepository;
import stu.mango.security.core.validate.code.ValidateCodeType;

import java.util.concurrent.TimeUnit;

/**
 * 针对app的服务请求。使用redis服务器完成对验证码的存储、获取、删除操作
 */
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    public RedisValidateCodeRepository(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(ServletWebRequest request, ValidateCodeType validateCodeType, ValidateCode code) {
        logger.info("save code" + code);
        redisTemplate.opsForValue().set(buildKey(request, validateCodeType), code, 30, TimeUnit.MINUTES);
    }

    private String buildKey(ServletWebRequest request, ValidateCodeType type) {
        String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("未在请求头中找到有效的deviceId参数。");
        }

        return "code" + type.toString() + ":" + deviceId;
    }

    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
        return (ValidateCode) redisTemplate.opsForValue().get(buildKey(request, validateCodeType));
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
        redisTemplate.delete(buildKey(request, validateCodeType));
    }
}
