package stu.mango.security.core.validate.code;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import stu.mango.security.core.properties.SecurityConstants;
import stu.mango.security.core.properties.SecurityProperties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private Map<String, ValidateCodeType> urlMap;

    private final SecurityProperties securityProperties;

    private final AuthenticationFailureHandler mangoAuthenticationFailureHandler;

    private final ValidateCodeProcessorHolder processorHolder;

    private boolean debug = logger.isDebugEnabled();

    @Autowired
    public ValidateCodeFilter(SecurityProperties securityProperties,
                              AuthenticationFailureHandler mangoAuthenticationFailureHandler,
                              ValidateCodeProcessorHolder processorHolder) {
        this.mangoAuthenticationFailureHandler = mangoAuthenticationFailureHandler;
        urlMap = new HashMap<>();
        this.securityProperties = securityProperties;
        this.processorHolder = processorHolder;
    }


    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        addUrlToMap(securityProperties.getCode().getImage().getUrl(), ValidateCodeType.IMAGE);

        urlMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);
        addUrlToMap(securityProperties.getCode().getSms().getUrl(), ValidateCodeType.SMS);

        if (debug) {
            urlMap.keySet().forEach(url -> logger.info("需要验证码验证的请求：" + url));
        }
    }

    /**
     * 初始化 ImageCodeFilter 过滤的请求集合
     *  <br/> &emsp;将用户所配置的需要使用验证码功能的请求的url加到准备到的set中;
     *  <br/> &emsp;默认的登录请求默认需要验证码
     */
    private void addUrlToMap(String urls, ValidateCodeType type) {
        if (StringUtils.isNotBlank(urls)) {
            String[] configFilterUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urls, ",");
            if (configFilterUrls != null) {
                for (String url : configFilterUrls) {
                    urlMap.put(url, type);
                }
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        ValidateCodeType validateCodeType = getValidateCodeType(request);
        if (validateCodeType != null) {

            logger.info("校验请求[" + request.getRequestURI() + "]中的验证码,验证码类型: " + validateCodeType);

            try {
                ValidateCodeProcessor processor = processorHolder.getValidateCodeProcessor(validateCodeType);

                processor.validate(new ServletWebRequest(request, response));
            } catch (ValidateCodeException e) {
                mangoAuthenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }

            logger.info("验证码校验完毕");
        }

        chain.doFilter(request, response);
    }

    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {

        String requestUrl = request.getRequestURI();
        ValidateCodeType result = null;

        if (MapUtils.isNotEmpty(urlMap) && !StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            final String[] urlBy = new String[1];
            urlMap.keySet().forEach(url -> {
                if (antPathMatcher.match(url, requestUrl)) {
                    urlBy[0] = url;
                }
            });

            result = urlMap.get(urlBy[0]);
        }

        return result;
    }
}
