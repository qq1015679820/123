package com.fh.cart.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.cart.service.CartService;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstInter;
import com.fh.member.model.Member;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("cartController")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("buy")
    public ServerResponse buy(Integer productId, int count, HttpServletRequest request){
        return cartService.buy(productId,count,request);
    }

    @RequestMapping("queryList")
    public ServerResponse queryList( HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(SystemConstInter.SESSION_KEY);
        List<String> stringList = RedisUtil.hget(SystemConstInter.CART_KEY + member.getId());
        List<Cart> list = new ArrayList<>();
        if (stringList.size()>0 && stringList!=null){
            for (String s : stringList) {
                Cart cart = JSONObject.parseObject(s, Cart.class);
                list.add(cart);
            }
        }else {
            return ServerResponse.error();
        }
        return ServerResponse.success(list);
    }

    @RequestMapping("deleteCart/{productId}")
    public ServerResponse deleteCart(HttpServletRequest request,@PathVariable("productId") Integer productId){
        Member member = (Member) request.getSession().getAttribute(SystemConstInter.SESSION_KEY);
        RedisUtil.hdel(SystemConstInter.CART_KEY + member.getId(),productId.toString());
        return ServerResponse.success();
    }
    @RequestMapping("deleteBatch")
    public ServerResponse deleteBatch(HttpServletRequest request,@RequestParam("idList") List<Integer> idList){
        Member member = (Member) request.getSession().getAttribute(SystemConstInter.SESSION_KEY);
        for (Integer productId : idList) {
            RedisUtil.hdel(SystemConstInter.CART_KEY +member.getId(),productId.toString());
        }
        return ServerResponse.success();
    }
    @RequestMapping("queryCartProductCount")
    public ServerResponse queryCartProductCount(HttpServletRequest request){
        Member member = (Member) request.getSession().getAttribute(SystemConstInter.SESSION_KEY);
        List<String> stringList = RedisUtil.hget(SystemConstInter.CART_KEY + member.getId());
        long ckeckCount=0;
        if (stringList.size()>0 && stringList!=null){
        for (String s : stringList) {
            Cart cart = JSONObject.parseObject(s, Cart.class);
            ckeckCount+=cart.getCount();
           }
        }else {
            return ServerResponse.success(0);
        }
        return ServerResponse.success(ckeckCount);
    }

    @RequestMapping("insertMember")
    public ServerResponse insertMember(){

        String s = UUID.randomUUID().toString();

        RedisUtil.set(s,s);

        return ServerResponse.success(s);
    }

}
