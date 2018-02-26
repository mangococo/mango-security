package stu.mango.security.rbac;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Component("rbacService")
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication auth) {
        final boolean[] hasPermission = {false};
        Object principal = auth.getPrincipal();
        /*
        已通过userDetailsService 进行过数据库查询并将相关信息放入了userDetails (非匿名用户)
        才能进行权限控制
         */
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();

            // 根据username 查询用户所拥有权限的所有URL
            Set<String> urls = getUrlsByUsername(username);
            urls.forEach(url -> {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission[0] = true;
                }
            });
        }

        return hasPermission[0];
    }

    private Set<String> getUrlsByUsername(String username) {

        return new HashSet<>();
    }
}

