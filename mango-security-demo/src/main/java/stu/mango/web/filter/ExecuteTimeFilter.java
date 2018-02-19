package stu.mango.web.filter;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

//@Component
public class ExecuteTimeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Time filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Time filter start");
        long time = new Date().getTime();
        chain.doFilter(request, response);
        System.out.println("execute time:" + (new Date().getTime() - time));
        System.out.println("Time filter end");
    }

    @Override
    public void destroy() {
        System.out.println("Time filter destroy");
    }
}
