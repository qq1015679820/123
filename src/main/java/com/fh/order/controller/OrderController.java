package com.fh.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.common.idempotent;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.order.service.order.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("orderController")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("paymentOrder")
    @idempotent
    public ServerResponse paymentOrder(String stringCart, Integer payType, Integer addressId, HttpServletRequest request){
        List<Cart> cartList = null;
        if (StringUtils.isNotEmpty(stringCart)){
            cartList = JSONObject.parseArray(stringCart, Cart.class);
        }
        return orderService.paymentOrder(cartList,payType,addressId,request);
    }

    @RequestMapping("queryList")
    public ServerResponse queryList(){
        List<OrderInfo> list = orderService.queryList();
        return ServerResponse.success(list);
    }
    @RequestMapping("queryOrderList")
    public ServerResponse queryOrderList(){
        List<Order> list = orderService.queryOrderList();
        return ServerResponse.success(list);
    }

}
