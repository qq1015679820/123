package com.fh.product.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.common.ServerResponse;
import com.fh.product.mapper.ProductMapper;
import com.fh.product.model.Product;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public List<Product> queryList() {
        return productMapper.selectList(null);
    }

    @Override
    public ServerResponse queryHotList() {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        QueryWrapper<Product> isHot = queryWrapper.eq("isHot", 1);
        List<Product> isHotList = productMapper.selectList(isHot);
        return ServerResponse.success(isHotList);
    }
    @Override
    public ServerResponse queryProductListPage(long currentPage, long pageSize) {
        //当前页数-1 乘于条数
        long start = (currentPage-1)*pageSize;
        //查询总条数
        long totalCount = productMapper.queryTotalCount();
        List<Product> list = productMapper.queryList(start,pageSize);
        long totalPage = totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
        Map map = new HashMap<>();
        map.put("list",list);
        map.put("totalPage",totalPage);

        return ServerResponse.success(map);
    }

    @Override
    public Product queryCartById(Integer productId) {
        Product product = productMapper.selectById(productId);
        return product;
    }

    @Override
    public Long updateStock(int id,Integer count) {
        return productMapper.updateStock(id,count);
    }

}
