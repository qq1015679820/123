package com.fh.member.service;

import com.fh.common.ServerResponse;
import com.fh.member.model.Member;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface MemberService {
    ServerResponse checkMemberName(String name);

    ServerResponse checkMemberPhone(String phone);

    ServerResponse addRegister(Member member);

    ServerResponse login(Member member, HttpServletResponse response);

}
