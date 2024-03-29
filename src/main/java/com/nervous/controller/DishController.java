package com.nervous.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nervous.common.R;
import com.nervous.dto.DishDto;
import com.nervous.entity.Category;
import com.nervous.entity.Dish;
import com.nervous.entity.DishFlavor;
import com.nervous.service.CategoryService;
import com.nervous.service.DishFlavorService;
import com.nervous.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info("DishDto:{}", dishDto.toString());
        //操作两张表
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功！");
    }

    /**
     * 菜品信息的分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //排序条件
        queryWrapper.orderByAsc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo, queryWrapper);
        //返回CategoryId转换为菜品分类
        //对象Copy
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");//忽略records
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类ID

            Category category = categoryService.getById(categoryId);//查询分类对象
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());


        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }
    /**
     * 修改菜品状态
     *
     * @param ids
     * @return
     */
    @PostMapping("/status/{ids}")
    public R<DishDto> post(@RequestParam String ids) {
        log.info("修改的ID:{}", ids);

        String[] listids = ids.split(",");
        DishDto[] dtos = new DishDto[listids.length];
        for (int i = 0; i < listids.length; i++) {
            log.info("ids"+listids[i]);
//            DishDto dishDto = dishService.getByIdWithFlavor(Long.valueOf(ids));
            DishDto dishDto = dishService.getByIdWithFlavor(Long.valueOf(listids[i]));

            //修改菜品的状态
            Integer status = dishDto.getStatus();
            if(status==1){
                status = 0;
            }else {
                status = 1;
            }
            dishDto.setStatus(status);
            dishService.updateWithFlavor(dishDto);
            dtos[i]=dishDto;
        }

        return R.success(dtos[0]);
    }

    /**
     * 修改菜品
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        log.info("修改的ID:{}", id);
        //查询两张表
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("DishDto:{}", dishDto.toString());
        //操作两张表
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功！");
    }
    /**
     * 根据ids删除菜品
     *
     * @param ids
     * @return
     */
    @DeleteMapping()
    public R<String> delete(@RequestParam String ids) {
        log.info("删除的ID:{}", ids);
        //查询两张表
        DishDto dishDto = dishService.getByIdWithFlavor(Long.valueOf(ids));
        dishService.deleteByIds(Long.valueOf(ids));
        return R.success("删除菜品成功");
    }


    /**
     * 根据条件来查询对应的菜品数据
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        //构造
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);//查询状态为1的菜品

        //排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();//分类ID

            Category category = categoryService.getById(categoryId);//查询分类对象
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            //查询口味数据
            Long dishId = item.getId();//菜品的ID
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);

            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}
