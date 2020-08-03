package com.fh.order.service;

import com.fh.order.model.Address;

import java.util.List;

public interface AddressService {
    List<Address> queryList();


    void updateStatus(Integer id);

    void del(Integer id);
}
