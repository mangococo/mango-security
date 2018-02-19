package stu.mango.security.core.validate.code;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图片验证码
 */
public class ValidateCode implements Serializable {
    /**
     * 随机数，即图片验证码中的内容
     */
    private String code;

    /**
     * 图形验证码的过期时间
     */
    private LocalDateTime expireTime;

    public ValidateCode(String code) {
        this.code = code;
    }

    /**
     * @param expireIn 过期时间
     */
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}

