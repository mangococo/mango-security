package stu.mango.security.core.validate.code.sms;

/**
 * 短信服务商发送验证码接口
 */
public interface SmsCodeSender {
    void send(String mobile, String code);
}
