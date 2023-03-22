package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.EmployeeService;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private EmployeeService employeeService;
    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：",orders.toString());
        orderService.submit(orders);

        return R.success("下单成功！");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
            log.info("查询订单明细了...");
        log.info("分页查询的page={},pageSize={},name={}",page,pageSize,name);
        //构造分页器
        Page pageInfo = new Page(page, pageSize);
        //构建条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper();
        //添加一个过滤条件
        queryWrapper.like(!StringUtils.isEmpty(name),Orders::getUserName,name);
        //添加一个排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
        //执行查询
        orderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

}
