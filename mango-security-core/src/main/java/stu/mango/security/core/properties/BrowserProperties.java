package stu.mango.security.core.properties;

public class BrowserProperties {
    /**
     * 登录页，若不设置，默认为 default-signin.html
     */
    private  String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;

    /**
     * 注册页，若不设置，默认为 default-signup.html
     */
    private  String signUpUrl = SecurityConstants.DEFAULT_SIGN_UP_PAGE_URL;

    /**
     * 退出也，若不设置，默认为空
     */
    private String signOutUrl;

    /**
     * 登录成功后的响应类型，若不设置，默认为返回json格式的用户信息
     */
    private LoginType loginType = LoginType.JSON;

    /**
     * 服务器记住用户的时限（秒），若不设置，默认为3600
     */
    private int rememberMeSeconds = 3600;

    private SessionProperties session = new SessionProperties();

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }

    public String getSignUpUrl() {
        return signUpUrl;
    }

    public void setSignUpUrl(String signUpUrl) {
        this.signUpUrl = signUpUrl;
    }

    public SessionProperties getSession() {
        return session;
    }

    public void setSession(SessionProperties session) {
        this.session = session;
    }

    public String getSignOutUrl() {
        return signOutUrl;
    }

    public void setSignOutUrl(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }
}
