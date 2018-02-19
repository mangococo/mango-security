package stu.mango.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 会拦截所有Controller（除去自定义Controller也包括Spring提供的Controller）方法
 * 但无法对方法参数进行操作
 *
 */
@Component
public class ExecuteTimeInterceptor implements HandlerInterceptor {
    /**
     * @param handler 所执行的 Controller
     * @return boolean 决定后续方法（post,after）是否要执行
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle: " + (HandlerMethod)handler);
        request.setAttribute("startTime", new Date().getTime());
        return true;
    }

    /*
    当Controller方法抛出异常时，该方法不会被调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
        long startTime = (long) request.getAttribute("startTime");
        System.out.println("execute time :" + (new Date().getTime() - startTime));
    }

    /**
     * 无论如何，该方法都会被调用
     * @param exception Controller方法所抛出的异常，无异常抛出时为null
     *                  但此处的异常只能接收到未被ControllerAdvice（异常处理器）处理的异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        System.out.println("postHandle");
        long startTime = (long) request.getAttribute("startTime");
        System.out.println("execute time :" + (new Date().getTime() - startTime));
        System.out.println("exception is " + exception);
    }
}
