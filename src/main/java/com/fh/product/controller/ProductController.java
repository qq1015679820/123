package com.fh.product.controller;

import com.fh.common.Ignore;
import com.fh.common.ServerResponse;
import com.fh.product.model.Product;
import com.fh.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("productController")
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("queryList")
    @Ignore
    public ServerResponse queryList(){
        List<Product> list = productService.queryList();
        return ServerResponse.success(list);
    }

    @RequestMapping("queryHotList")
    @Ignore
    public ServerResponse queryHotList(){
        return productService.queryHotList();
    }

    @RequestMapping("queryProductListPage")
    @Ignore
    public ServerResponse queryProductListPage(long currentPage ,long pageSize){
        return productService.queryProductListPage(currentPage,pageSize);
    }

}
