package com.fh.Interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.Ignore;
import com.fh.common.LoginException;
import com.fh.common.MyException;
import com.fh.common.SystemConstInter;
import com.fh.member.model.Member;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLDecoder;

public class LoginInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"x-auth,mtoken,content-type");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"PUT,POST,DELETE,GET");
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method =  handlerMethod.getMethod();
        if (method.isAnnotationPresent(Ignore.class)){
            return true;
        }

        //进行登录认证
        String token =request.getHeader("x-auth");
            if (StringUtils.isEmpty(token)){
            throw new LoginException();
        }
       request.getSession().setAttribute("token",token);
        String s = RedisUtil.get(token);
            if (s==null){
                throw new MyException();
            }

        boolean verify = JwtUtil.verify(token);
        if (verify){
            String user = JwtUtil.getUser(token);
            String decode = URLDecoder.decode(user, "utf-8");
            Member member = JSONObject.parseObject(decode, Member.class);
            request.getSession().setAttribute(SystemConstInter.SESSION_KEY,member);
        }else {
            throw new LoginException();
        }
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
