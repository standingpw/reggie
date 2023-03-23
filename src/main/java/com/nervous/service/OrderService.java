package com.nervous.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nervous.entity.Orders;

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);


}
