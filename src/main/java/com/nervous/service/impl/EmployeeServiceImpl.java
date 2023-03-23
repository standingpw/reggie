package com.nervous.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nervous.entity.Employee;
import com.nervous.mapper.EmployeeMapper;
import com.nervous.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
