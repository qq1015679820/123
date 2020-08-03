package com.fh.member.controller;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstInter;
import com.fh.member.model.Member;
import com.fh.member.service.MemberService;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("memberController")
public class MemberController {

    @Autowired
    private MemberService memberService;
    @RequestMapping(value = "checkMemberName")
    @Ignore
    public ServerResponse checkMemberName(String name)  {
        return  memberService.checkMemberName(name);
    }
    @RequestMapping("checkMemberPhone")
    @Ignore
    public ServerResponse checkMemberPhone(String phone)  {
        return  memberService.checkMemberPhone(phone);
    }
    @RequestMapping("addRegister")
    @Ignore
    public ServerResponse addRegister(Member member)  {
        return  memberService.addRegister(member);
    }
    @RequestMapping("login")
    @Ignore
    public ServerResponse login(Member member, HttpServletResponse response){
        return  memberService.login(member,response);
    }

    @RequestMapping("checkLogin")
    @Ignore
    public ServerResponse checkLogin(HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(SystemConstInter.SESSION_KEY);
        if (member == null){
            return ServerResponse.error();
        }
        return ServerResponse.success();
    }

    @RequestMapping("exit")
    @Ignore
    public ServerResponse exit(HttpServletRequest request){
        String token = (String) request.getSession().getAttribute("token");
        RedisUtil.del(token);
        return ServerResponse.success();
    }

    @RequestMapping("test")
    public ServerResponse test(){
        return ServerResponse.success();
    }



}
