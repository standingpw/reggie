package com.nervous.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nervous.entity.DishFlavor;
import com.nervous.mapper.DishFlavorMapper;
import com.nervous.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
