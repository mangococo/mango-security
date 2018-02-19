package stu.mango.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import stu.mango.web.filter.ExecuteTimeFilter;
import stu.mango.web.interceptor.ExecuteTimeInterceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    private final ExecuteTimeInterceptor interceptor;

    @Autowired
    public WebConfig(ExecuteTimeInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    /**
     * SpringBoot 配置自定义或第三方 Filter
     */
//    @Bean
    public FilterRegistrationBean executeTimeFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

        ExecuteTimeFilter timeFilter = new ExecuteTimeFilter();
        filterRegistrationBean.setFilter(timeFilter);

        List<String> filterUrls = new ArrayList<>();
        filterUrls.add("/*");
        filterRegistrationBean.setUrlPatterns(filterUrls);

        return filterRegistrationBean;
    }

    /**
     * 同步处理下，注册拦截器 interceptor 的方法
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(interceptor);
    }

    /**
     * 异步处理配置
     * @param configurer 异步支撑配置对象
     */
//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//        // 异步处理时，注册拦截器的方法
//        configurer.registerCallableInterceptors(); // 增添了handleTimeOut方法，即处理超时的处理
//        configurer.registerDeferredResultInterceptors();
//
//        // 设定异步处理时间
//        configurer.setDefaultTimeout(1000);
//
//        // 用自定义线程池（可重用）去替代spring默认线程池（不可重用）
//        // configurer.setTaskExecutor()
//    }

}
