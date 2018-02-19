package stu.mango.security.app;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.stereotype.Component;
import stu.mango.security.core.properties.SecurityConstants;

@Component
public class SpringSocialConfigurePostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!StringUtils.equals("mangoSpringSocialConfigurer", beanName)) {
            return bean;
        }

        SpringSocialConfigurer springSocialConfigurer = (SpringSocialConfigurer) bean;
        springSocialConfigurer.signupUrl(SecurityConstants.DEFAULT_SIGN_UP_URL_APP);

        return springSocialConfigurer;
    }
}
