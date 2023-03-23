package com.nervous.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nervous.dto.DishDto;
import com.nervous.entity.Dish;
import com.nervous.entity.DishFlavor;
import com.nervous.mapper.DishMapper;
import com.nervous.service.DishFlavorService;
import com.nervous.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;

    //新增菜品以及保存口味
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存dish表基本信息
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品的id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存到口味信息表中dish_flavor
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 根据id查询菜品和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //1、查询菜品基本信息dish
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        //2、查询当前菜品对应的口味信息dishFlavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public DishDto updateWithFlavor(DishDto dishDto) {
        //更新菜品表dish
        this.updateById(dishDto);
        //更新口味表dish_flavor,先清理当前菜品的口味delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //再提交口味数据--dish_flavor insert操作

        Long dishId = dishDto.getId();//菜品的id
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);

        return null;
    }

    @Override
    public DishDto deleteByIds(Long id) {
        //删除dish,首先要删除其对应的口味，要查找出对应的口味
        DishDto flavor = dishService.getByIdWithFlavor(id);
        List<DishFlavor> flavors = flavor.getFlavors();

        for (DishFlavor dishFlavor : flavors) {
            Long flavorId = dishFlavor.getId();
            dishFlavorService.removeById(flavorId);
        }
        dishService.removeById(id);
        return null;
    }



}
