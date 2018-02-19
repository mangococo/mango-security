package stu.mango.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

public interface ValidateCodeRepository {
    /**
     * 存储验证码
     * @param request
     * @param validateCodeType
     * @param code
     */
    void save(ServletWebRequest request, ValidateCodeType validateCodeType, ValidateCode code);

    /**
     * 获取验证码
     * @param request
     * @param validateCodeType
     * @return
     */
    ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);

    /**
     * 移除验证码
     * @param request
     * @param validateCodeType
     */
    void remove(ServletWebRequest request, ValidateCodeType validateCodeType);
}
