package stu.mango.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

public interface ValidateCodeGenerator {
    ValidateCode createValidateCode(ServletWebRequest request);
}
