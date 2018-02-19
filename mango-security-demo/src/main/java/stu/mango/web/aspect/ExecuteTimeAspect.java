package stu.mango.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

// @Aspect
// @Component
public class ExecuteTimeAspect {
    @Around("execution(* stu.mango.web.controller.UserController.*(..))")
    public Object handleControllerMethod(ProceedingJoinPoint point) throws Throwable {

        System.out.println("time aspect start");
        long startTime = new Date().getTime();
        Object handler = point.proceed();
        System.out.println("总耗时：" + (new Date().getTime() - startTime));

        for (Object o : point.getArgs()) {
            System.out.println("[" + o.getClass().getSimpleName() + "]:" + o);
        }

        System.out.println("_______________" + handler);

        System.out.println("time aspect end");

        return handler;
    }
}
