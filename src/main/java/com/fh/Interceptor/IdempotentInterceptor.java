package com.fh.Interceptor;

import com.fh.common.LoginException;
import com.fh.common.MyException;
import com.fh.common.idempotent;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class IdempotentInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method =  handlerMethod.getMethod();
        if (!method.isAnnotationPresent(idempotent.class)){
            return true;
        }

        String token =request.getHeader("mtoken");
        if (StringUtils.isEmpty(token)){
            throw new MyException();
        }
        Boolean aBoolean = RedisUtil.exists(token);
        if (!aBoolean){
            throw new MyException();
        }

         Long del = RedisUtil.del(token);
        if (del==0){
            throw new MyException();

        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
