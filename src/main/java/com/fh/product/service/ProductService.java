package com.fh.product.service;


import com.fh.common.ServerResponse;
import com.fh.product.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> queryList();

    ServerResponse queryHotList();

    ServerResponse queryProductListPage(long currentPage, long pageSize);

    Product queryCartById(Integer productId);

    Long updateStock(int id, Integer count);
}
