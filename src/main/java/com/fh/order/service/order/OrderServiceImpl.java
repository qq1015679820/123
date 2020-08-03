package com.fh.order.service.order;

import com.alibaba.fastjson.JSONObject;
import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.common.SystemConstInter;
import com.fh.member.model.Member;
import com.fh.order.mapper.OrderInfoMapper;
import com.fh.order.mapper.OrderMapper;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import com.fh.util.BigDecimalUtil;
import com.fh.util.IdUtil;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ProductService productService;

    @Override
    public ServerResponse paymentOrder(List<Cart> cartList, Integer payType, Integer addressId, HttpServletRequest request) {
        Member member = (Member) request.getSession().getAttribute(SystemConstInter.SESSION_KEY);
        String orderId = IdUtil.createId();
        List<OrderInfo> orderInfoList = new ArrayList<>();
        BigDecimal totalPrice = new BigDecimal("0.00");
        List<String> stockNotFull  = new ArrayList<>();
        for (Cart cart : cartList) {
            Product product = productService.queryCartById(cart.getProductId());
            if(product.getStock()<cart.getCount()){
                stockNotFull.add(cart.getName());
            }
            Long res = productService.updateStock(product.getId(),cart.getCount());
            if(res==1){
                OrderInfo orderInfo = buildOrderInfo(orderId, cart);
                orderInfoList.add(orderInfo);
                BigDecimal subTotal = BigDecimalUtil.mul(cart.getPrice().toString(),cart.getCount()+"");
                totalPrice = BigDecimalUtil.add(totalPrice,subTotal);
            }else{
                stockNotFull.add(cart.getName());
            }
        }
        if(orderInfoList !=null && orderInfoList.size()==cartList.size() ){
            //库存都足  保存订单详细
            for (OrderInfo orderInfo : orderInfoList) {
                orderInfoMapper.insert(orderInfo);
                //更新redis购物车
                updateRedisCart(member,orderInfo);
            }
            //  生成订单
            buildOrder(payType, addressId, member, orderId, totalPrice);
            return ServerResponse.success(orderId);
        }else{
            return ServerResponse.error(stockNotFull);

        }
        }

    @Override
    public List<OrderInfo> queryList() {
        return orderInfoMapper.selectList(null);
    }

    @Override
    public List<Order> queryOrderList() {
        return orderMapper.selectList(null);
    }

    private void buildOrder(Integer payType, Integer addressId, Member member, String orderId, BigDecimal totalPrice) {
        Order order = new Order();
        order.setCreateDate(new Date());
        order.setPayType(payType);
        order.setAddressId(addressId);
        order.setId(orderId);
        order.setMemberId(member.getId());
        order.setTotalPrice(totalPrice);
        order.setStatus(SystemConstInter.ORDER_STATUS_WAIT);
        orderMapper.insert(order);
    }

    private OrderInfo buildOrderInfo(String orderId, Cart cart) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setName(cart.getName());
        orderInfo.setFilePath(cart.getFilePath());
        orderInfo.setPrice(cart.getPrice());
        orderInfo.setOrderId(orderId);
        orderInfo.setProductId(cart.getProductId());
        orderInfo.setCount(cart.getCount());
        return orderInfo;
    }

    private void updateRedisCart(Member member, OrderInfo orderInfo) {
        String cartJson = RedisUtil.hGet(SystemConstInter.CART_KEY + member.getId(), orderInfo.getProductId().toString());
        if(StringUtils.isNotEmpty(cartJson)){
            Cart cart1 = JSONObject.parseObject(cartJson, Cart.class);
            if(cart1.getCount()<=orderInfo.getCount()){
                //删除购物车中该商品
                RedisUtil.hdel(SystemConstInter.CART_KEY + member.getId(), orderInfo.getProductId().toString());

            }else{
                //更新购物车
                cart1.setCount(cart1.getCount()-orderInfo.getCount());
                String s = JSONObject.toJSONString(cart1);
                RedisUtil.hSet(SystemConstInter.CART_KEY + member.getId(), orderInfo.getProductId().toString(),s);
            }

        }
    }

    }
