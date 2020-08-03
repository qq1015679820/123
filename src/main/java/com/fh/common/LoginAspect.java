/*
package com.fh.common;


import com.fh.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Address;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Address(1)
public class LoginAspect {


    @Autowired
    private HttpServletRequest httpServletRequest;

//    @Around("execution(* com.fh.demo.controller.*.*(..)) && @annotation(loginAnnotation)")
     @Around("@annotation(ignore)&& execution(* com.fh.*.controller.*.*(..)) ")
    public ServerResponse loginAround(ProceedingJoinPoint joinPoint, Ignore ignore) {


        Map<String,Object> map=new HashMap<>();
        try {
            //进行登录认证
            String token =httpServletRequest.getHeader("x-auth");
            System.out.println(token);
            if( JwtUtil.verify(token)){
                joinPoint.proceed();
                return ServerResponse.success();
            }else{
                return ServerResponse.token_error();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return ServerResponse.token_error();
    }
}
*/
