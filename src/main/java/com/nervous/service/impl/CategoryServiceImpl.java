package com.nervous.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nervous.common.CustomException;
import com.nervous.entity.Category;
import com.nervous.entity.Dish;
import com.nervous.entity.Setmeal;
import com.nervous.mapper.CategoryMapper;
import com.nervous.service.CategoryService;
import com.nervous.service.DishService;
import com.nervous.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id来删除 分类，删除之前需要判断是否关联了菜品
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);

        //是否关联了菜品，关联了则抛出业务异常，
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            //已经关联了菜品，抛出业务异常
            throw new CustomException("当前分类项关联了菜品，不能删除！");
        }
        //分类是否关联了套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //已经关联了套餐，抛出业务异常
            throw new CustomException("当前分类项关联了套餐，不能删除！");

        }
        //可以正常删除我们的分类
        super.removeById(id);

    }
}
