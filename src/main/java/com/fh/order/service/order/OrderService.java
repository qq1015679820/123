package com.fh.order.service.order;

import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;
import com.fh.order.model.Order;
import com.fh.order.model.OrderInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    ServerResponse paymentOrder(List<Cart> cartList, Integer payType, Integer addressId, HttpServletRequest request);

    List<OrderInfo> queryList();

    List<Order> queryOrderList();

}
