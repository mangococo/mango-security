package stu.mango.security.core.properties;

public class BrowserProperties {
    /**
     * 登录页，若不设置，默认为 default-signin.html
     */
    private  String signInUrl = SecurityConstants.DEFAULT_SIGN_IN_PAGE_URL;

    /**
     * 注册页，若不设置，默认为 default-signup.html
     */
    private  String signUpUrl = SecurityConstants.DEFAULT_SIGN_UP_PAGE_URL;

    /**
     * 退出也，若不设置，默认为空
     */
    private String signOutUrl = SecurityConstants.DEFAULT_SIGN_OUT_URL;

    /**
     * 登录成功后的响应类型，若不设置，默认为返回json格式的用户信息
     */
    private LoginType loginType = LoginType.JSON;

    /**
     * 服务器记住用户的时限（秒），若不设置，默认为3600
     */
    private int rememberMeSeconds = 3600;

    /**
     * 在第一次启动时创建 token 存储表，默认为 false
     */
    private boolean createTokenRepositoryTableOnStartup = false;

    private SessionProperties session = new SessionProperties();

    public String getSignInUrl() {
        return signInUrl;
    }

    public void setSignInUrl(String signInUrl) {
        this.signInUrl = signInUrl;
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

    public boolean isCreateTokenRepositoryTableOnStartup() {
        return createTokenRepositoryTableOnStartup;
    }

    public void setCreateTokenRepositoryTableOnStartup(boolean createTokenRepositoryTableOnStartup) {
        this.createTokenRepositoryTableOnStartup = createTokenRepositoryTableOnStartup;
    }
}
