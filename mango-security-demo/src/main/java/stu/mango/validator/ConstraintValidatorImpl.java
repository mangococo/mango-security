package stu.mango.validator;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import stu.mango.web.service.HelloService;
import stu.mango.validator.annotation.MyConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验逻辑类实现ConstraintValidator，Spring会默认将其作为Bean
 */
public class ConstraintValidatorImpl implements ConstraintValidator<MyConstraint, Object> {
    Log logger = LoggerFactory.make();

    @Autowired
    private HelloService helloService;

    /**
     * 校验初始化
     * @param myConstraint 校验注解
     */
    @Override
    public void initialize(MyConstraint myConstraint) {
        logger.debug("my constraint validator init");
    }

    /**
     * 校验逻辑
     * @param value 需要校验的值
     * @return 校验结果（true or false）
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        // helloService.greeting("Tom");
        logger.debug("MyConstraint value is" + value);
        return value != null;
    }
}
