package com.fh.cart.service;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstInter;
import com.fh.member.model.Member;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductService productService;

    @Override
    public ServerResponse buy(Integer productId, int count, HttpServletRequest request) {
       Product product= productService.queryCartById(productId);
       if (product==null){
           return ServerResponse.error("商品不存在");
       }
       if (product.getStatus()==0){
           return ServerResponse.error("商品已下架");
       }
        Member member = (Member) request.getSession().getAttribute(SystemConstInter.SESSION_KEY);
        boolean exists = RedisUtil.hExists(SystemConstInter.CART_KEY+member.getId(), productId.toString());
       if (!exists){
           Cart cart = new Cart();
           cart.setCount(count);
           cart.setFilePath(product.getFilePath());
           cart.setName(product.getName());
           cart.setProductId(productId);
           cart.setPrice(product.getPrice());
           String jsonString = JSONObject.toJSONString(cart);
           RedisUtil.hSet(SystemConstInter.CART_KEY+member.getId(),productId.toString(),jsonString);
       }else {
           String productJson = RedisUtil.hGet(SystemConstInter.CART_KEY + member.getId(), productId.toString());
           Cart cart = JSONObject.parseObject(productJson, Cart.class);
           cart.setCount(cart.getCount()+count);

           String jsonString = JSONObject.toJSONString(cart);
           RedisUtil.hSet(SystemConstInter.CART_KEY+member.getId(),productId.toString(),jsonString);
       }
        return ServerResponse.success();
    }
}
