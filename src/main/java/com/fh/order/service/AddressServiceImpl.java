package com.fh.order.service;

import com.fh.order.mapper.AddressMapper;
import com.fh.order.model.Address;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressMapper addressMapper;
    @Override
    public List<Address> queryList() {
        return addressMapper.selectList(null);
    }

    @Override
    public void updateStatus(Integer id) {
        List<Address> list = addressMapper.selectList(null);
        for (Address order : list) {
            order.setStatus(0);
            addressMapper.updateById(order);
        }
        Address order = addressMapper.selectById(id);
        order.setStatus(1);
        addressMapper.updateById(order);
    }

    @Override
    public void del(Integer id) {
        addressMapper.deleteById(id);
    }
}
