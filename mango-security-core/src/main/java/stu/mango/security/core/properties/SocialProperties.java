package stu.mango.security.core.properties;

public class SocialProperties {

    private String filterProcessUrl = "/auth";

    private String filterFailureUrl = "/signin";

    private QQProperties qq = new QQProperties();

    private WeChatProperties wechat = new WeChatProperties();

    public QQProperties getQq() {
        return qq;
    }

    public void setQq(QQProperties qq) {
        this.qq = qq;
    }

    public String getFilterProcessUrl() {
        return filterProcessUrl;
    }

    public void setFilterProcessUrl(String filterProcessUrl) {
        this.filterProcessUrl = filterProcessUrl;
    }

    public String getFilterFailureUrl() {
        return filterFailureUrl;
    }

    public void setFilterFailureUrl(String filterFailureUrl) {
        this.filterFailureUrl = filterFailureUrl;
    }

    public WeChatProperties getWechat() {
        return wechat;
    }

    public void setWechat(WeChatProperties wechat) {
        this.wechat = wechat;
    }
}
