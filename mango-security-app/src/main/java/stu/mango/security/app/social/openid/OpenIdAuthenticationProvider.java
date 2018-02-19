package stu.mango.security.app.social.openid;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.UsersConnectionRepository;
import stu.mango.security.core.authentication.mobile.SmsCodeAuthenticationToken;

import java.util.HashSet;
import java.util.Set;

public class OpenIdAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    /**
     * userconnection 第三方关系表
     */
    private UsersConnectionRepository usersConnectionRepository;

    OpenIdAuthenticationProvider(UserDetailsService userDetailsService, UsersConnectionRepository usersConnectionRepository) {
        this.userDetailsService = userDetailsService;
        this.usersConnectionRepository = usersConnectionRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OpenIdAuthenticationToken token = (OpenIdAuthenticationToken) authentication;


        Set<String> providerUserIds = new HashSet<>();
        providerUserIds.add((String) token.getPrincipal());
        Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo(token.getProviderId(), providerUserIds);

        if (CollectionUtils.isEmpty(userIds) || userIds.size() != 1) {
            throw new InternalAuthenticationServiceException("无法获取用户信息[未查询到有效的用户ID]");
        }

        String userId = userIds.iterator().next();
        UserDetails user = userDetailsService.loadUserByUsername(userId);

        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息[未查询到有效的用户]");
        }

        OpenIdAuthenticationToken authenticationToken = new OpenIdAuthenticationToken(user, user.getAuthorities());
        authenticationToken.setDetails(authentication.getDetails());

        return authenticationToken;
    }

    /**
     * 供AuthenticationManager为SmsCodeAuthenticationToken选择该Provider
     *
     * @param authentication 由Filter生成的Token
     * @return 是支持那一类型的验证
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return OpenIdAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
