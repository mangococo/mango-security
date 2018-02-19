package stu.mango.security.core.validate.code.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import stu.mango.security.core.validate.code.*;

import java.util.Map;

public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {
    /**
     * session操作工具
     */
    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    /**
     * Spring 会自动收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
     *
     * 以Bean的名字作为 key 值，bean 作为健
     */
    private final Map<String, ValidateCodeGenerator> generatorMap;

    @Autowired
    public AbstractValidateCodeProcessor(Map<String, ValidateCodeGenerator> generatorMap) {
        this.generatorMap = generatorMap;
    }

    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode = generate(request);
        save(request, validateCode);
        send(request, validateCode);
    }

    /**
     * 生成校验码
     */
    @SuppressWarnings("unchecked")
    private C generate(ServletWebRequest request) {
        String type = getValidateCodeType().toString().toLowerCase();
        ValidateCodeGenerator generator = generatorMap.get(type + "CodeGenerator");

        return (C) generator.createValidateCode(request);
    }

    /**
     * 将生成的校验码保存到 session中
     */
    private void save(ServletWebRequest request, C validateCode) {
        ValidateCode code = new ValidateCode(validateCode.getCode());
        code.setExpireTime(validateCode.getExpireTime());
        validateCodeRepository.save(request, getValidateCodeType(), code);
    }

    /**
     * 根据请求的url获取验证码的类型
     */
    private ValidateCodeType getValidateCodeType() {
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    /**
     * 验证码判断逻辑
     *
     * @param request 需要根据请求从其session中取出服务器保存的验证码
     * @throws AuthenticationException 用户身份验证失败
     */
    @Override
    @SuppressWarnings("unchecked")
    public void validate(ServletWebRequest request) {

        ValidateCodeType codeType = getValidateCodeType();

        C validateCode = (C) validateCodeRepository.get(request, codeType);

        String codeInRequest;// = ServletRequestUtils.getStringParameter(request.getRequest(), "smsCode");

        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("校验验证码失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (validateCode == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (validateCode.isExpire()) {
            validateCodeRepository.remove(request, codeType);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(validateCode.getCode(), codeInRequest)) {
            throw new ValidateCodeException("输入的验证码不匹配");
        }

        validateCodeRepository.remove(request, codeType);
    }
}
