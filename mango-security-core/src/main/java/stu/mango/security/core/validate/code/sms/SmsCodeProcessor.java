package stu.mango.security.core.validate.code.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import stu.mango.security.core.validate.code.ValidateCode;
import stu.mango.security.core.validate.code.ValidateCodeGenerator;
import stu.mango.security.core.validate.code.impl.AbstractValidateCodeProcessor;

import java.util.Map;

@Component("smsCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor {
    private final SmsCodeSender smsCodeSender;

    @Autowired
    public SmsCodeProcessor(SmsCodeSender smsCodeSender,
                            Map<String, ValidateCodeGenerator> generatorMap) {
        super(generatorMap);
        this.smsCodeSender = smsCodeSender;
    }

    @Override
    protected void send(ServletWebRequest request, ValidateCode smsCode) throws Exception {
        smsCodeSender.send(ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile"), smsCode.getCode());
    }
}
