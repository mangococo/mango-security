package stu.mango.security.core.validate.code.sms;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import stu.mango.security.core.properties.ImageCodeProperties;
import stu.mango.security.core.properties.SecurityProperties;
import stu.mango.security.core.validate.code.ValidateCode;
import stu.mango.security.core.validate.code.ValidateCodeGenerator;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class DefaultSmsCodeGenerator implements ValidateCodeGenerator {
    private final SecurityProperties securityProperties;

    public DefaultSmsCodeGenerator(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public ValidateCode createValidateCode(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}
