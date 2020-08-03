package com.fh.order.controller;

import com.fh.common.ServerResponse;
import com.fh.order.mapper.AddressMapper;
import com.fh.order.model.Address;
import com.fh.order.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("addressController")
public class AddressController {

    @Resource
    private AddressMapper addressMapper;

    @Autowired
    private AddressService addressService;

    @RequestMapping("queryList")
    public ServerResponse queryList(){
        List<Address> list = addressService.queryList();
        return ServerResponse.success(list);
    }

    @RequestMapping("updateStatus/{id}")
    public ServerResponse updateStatus(@PathVariable("id") Integer id){
        addressService.updateStatus(id);
        return ServerResponse.success();
    }


    @RequestMapping("del/{id}")
    public ServerResponse del(@PathVariable("id") Integer id){
        addressService.del(id);
        return ServerResponse.success();
    }


}
