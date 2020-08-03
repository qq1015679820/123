package com.fh.cart.service;

import com.fh.cart.model.Cart;
import com.fh.common.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CartService {
    ServerResponse buy(Integer productId, int count, HttpServletRequest request);

}
