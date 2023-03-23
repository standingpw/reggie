package com.nervous.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nervous.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
