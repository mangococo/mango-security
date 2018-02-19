package stu.mango.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class DemoUserDetailService implements UserDetailsService, SocialUserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public DemoUserDetailService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查找(数据库)用户信息
        logger.info("表单登录 用户名：" + username);

        return buildUser(username);
    }

    /**
     * SocialUserDetailsService
     *
     * @param userId 存储在userconnection表中的userId(或其他的唯一标识（用户名也是可以）)。用于社交登录
     */
    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        // 根据用户id或其它查找(数据库)用户信息
        logger.info("第三方社交登录 用户ID：" + userId);
        return buildUser(userId);


    }

    private SocialUserDetails buildUser(String userId) {
        // mock 模拟从数据库读到的已加密的密码
        String password = passwordEncoder.encode("password");
        logger.info("Password from database: \"" + password + "\"");

        return new SocialUser(userId, password,
                true, true, true, true
                , AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER")); // 将用","隔开的字符串转换为权限集合; 只有含有ROLE_USER的用户才可申请授权
    }
}
