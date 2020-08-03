package com.fh.member.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.member.mapper.MemberMapper;
import com.fh.member.model.Audience;
import com.fh.member.model.Member;
import com.fh.util.JwtTokenUtil;
import com.fh.util.JwtUtil;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private Audience audience;


    @Override
    public ServerResponse checkMemberName(String name) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",name);
        Member member = memberMapper.selectOne(queryWrapper);
        if (member!=null){
            return ServerResponse.error("用户已存在");
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse checkMemberPhone(String phone) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        Member member = memberMapper.selectOne(queryWrapper);
        if (member!=null){
            return ServerResponse.error("手机号已存在");
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse addRegister(Member member) {
        String redisCode = RedisUtil.get(member.getPhone());
        if (!redisCode.equals(member.getCode().toString())){
            return ServerResponse.error("验证码错误");
        }
        memberMapper.insert(member);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse login(Member member, HttpServletResponse response) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Member> name = queryWrapper.eq("name", member.getName());
        Member member1 = memberMapper.selectOne(name);
        if(member1==null){
            return ServerResponse.error("账号不存在");
        }
        if(!member.getPassword().equals(member1.getPassword())){
            return ServerResponse.error("密码错误");
        }

        String token = null;
        try {
            String jsonString = JSONObject.toJSONString(member1);
            String encode = URLEncoder.encode(jsonString, "utf-8");
            token = JwtUtil.sign(encode);
            RedisUtil.set(token,token,30*60*1000);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ServerResponse.success(token);
    }


}
