package com.fh.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobleHandleException {


    @ExceptionHandler(LoginException.class)
    public ServerResponse handleLoginException(LoginException e){
       // e.printStackTrace();
        return ServerResponse.login_error();
    }
    @ExceptionHandler(MyException.class)
    public ServerResponse handleMyException(Exception e){
        //e.printStackTrace();
        return ServerResponse.error();
    }

    @ExceptionHandler(Exception.class)
    public ServerResponse handleException(Exception e){
        e.printStackTrace();
        return ServerResponse.error();
    }

}
