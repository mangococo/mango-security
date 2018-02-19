package stu.mango.security.core.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

public class WeChatProperties extends SocialProperties {
    private String providerId = "wechat";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
