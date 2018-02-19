package stu.mango.security.core.properties;

public class SmsCodeProperties {
    /**
     * 验证码长度
     */
    private int length = 6;

    /**
     * 有效时间（秒）
     */
    private int expireIn = 60;

    /**
     * 需要验证的 url，用 "," 隔开
     */
    private String url;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
