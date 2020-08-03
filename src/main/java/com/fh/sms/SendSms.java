package com.fh.sms;

import com.aliyuncs.exceptions.ClientException;
import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.member.service.MemberService;
import com.fh.util.MessageVerifyUtils;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sendSms")
public class SendSms {

    @Autowired
    private MemberService memberService;

    @RequestMapping("sendSms")
    @Ignore
    public ServerResponse sendSms(String phone){
        ServerResponse serverResponse = memberService.checkMemberPhone(phone);
        if (serverResponse.getCode()!=200){
            return ServerResponse.error("该手机号已被注册请更换手机号");
        }else {
        try {
            int newcode = MessageVerifyUtils.getNewcode();
            MessageVerifyUtils.sendSms(phone,String.valueOf(newcode));
            RedisUtil.set(phone,String.valueOf(newcode));
        } catch (ClientException e) {
            e.printStackTrace();
         }
        }
        return ServerResponse.success("发送成功");
    }

}
