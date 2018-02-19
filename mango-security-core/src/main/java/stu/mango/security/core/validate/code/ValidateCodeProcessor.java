package stu.mango.security.core.validate.code;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器，用于封装不同的校验码处理逻辑
 */
public interface ValidateCodeProcessor {
   /**
     * 创建验证码
     * @param request 工具类，可以封装HttpServletRequest和response
     */
    void create(ServletWebRequest request) throws Exception;

    void validate(ServletWebRequest request) throws ServletRequestBindingException;
}
