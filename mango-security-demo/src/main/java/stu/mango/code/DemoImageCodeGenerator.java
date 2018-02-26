package stu.mango.code;

import org.springframework.web.context.request.ServletWebRequest;
import stu.mango.security.core.validate.code.ValidateCode;
import stu.mango.security.core.validate.code.ValidateCodeGenerator;

// @Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {
    @Override
    public ValidateCode createValidateCode(ServletWebRequest request) {
        System.out.println("用户自定义图形验证码船舰逻辑！");
        return null;
    }
}
