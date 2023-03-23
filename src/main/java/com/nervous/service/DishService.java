package com.nervous.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nervous.dto.DishDto;
import com.nervous.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，以及口味数据，需要操作两张表
    public void saveWithFlavor(DishDto dishDto);
    //根据id查询菜品和口味信息
    public DishDto getByIdWithFlavor(Long id);
    public DishDto updateWithFlavor(DishDto dishDto);

    public DishDto deleteByIds(Long valueOf);
}
