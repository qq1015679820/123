package com.fh.common;

public class ServerResponse {

    private Integer code;
    private String msg;
    private Object data;


    public static ServerResponse success(Object data){
        return new ServerResponse(200,"操作成功",data);
    }

    public static ServerResponse success(){
        return new ServerResponse(200,"操作成功");
    }

    public static ServerResponse error(){
        return new ServerResponse(1001,"操作失败");
    }
    public static ServerResponse error(Object data){
        return new ServerResponse(1001,"操作失败",data);
    }


    public static ServerResponse login_error(){
        return new ServerResponse(1002,"登录失败");
    }
    public static ServerResponse login_error(Object data){
        return new ServerResponse(1002,"登录失败",data);
    }

    public ServerResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ServerResponse() {
    }

    public ServerResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
