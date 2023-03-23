package com.nervous.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nervous.entity.OrderDetail;
import com.nervous.mapper.OrderDetailMapper;
import com.nervous.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
