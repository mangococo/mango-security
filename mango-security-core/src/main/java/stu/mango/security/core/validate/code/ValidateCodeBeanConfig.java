package stu.mango.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import stu.mango.security.core.properties.SecurityProperties;
import stu.mango.security.core.validate.code.image.DefaultImageCodeGenerator;
import stu.mango.security.core.validate.code.sms.DefaultSmsCodeGenerator;
import stu.mango.security.core.validate.code.sms.DefaultSmsCodeSender;
import stu.mango.security.core.validate.code.sms.SmsCodeSender;

@Configuration
public class ValidateCodeBeanConfig {
    private final SecurityProperties securityProperties;

    @Autowired
    public ValidateCodeBeanConfig(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    /**
     * ConditionalOnMissingBean 注解，spring在初始化这个bean时，会先去检查bean容器中<br/>
     * 是否已经存在了name为 imageCodeGenerator 的bean,若已存在，便不会再初始化它。
     * <br/>
     *
     * 也就起到了"允许用户自定义实现验证码创建方案"覆盖默认实现的目的（“开闭原则”，美滋滋 ）
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator() {
        return new DefaultImageCodeGenerator(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsCodeGenerator")
    public ValidateCodeGenerator smsCodeGenerator() {
        return new DefaultSmsCodeGenerator(securityProperties);
    }

    /**
     * 默认的短信验证码发送实现（仅控制台输出），如需该服务请自行声明实现 SmsCodeSender
     * 接口的 bean
     */
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
