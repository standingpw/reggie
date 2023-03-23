package com.nervous.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nervous.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
