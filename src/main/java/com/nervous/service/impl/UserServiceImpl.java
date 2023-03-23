package com.nervous.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nervous.entity.User;
import com.nervous.mapper.UserMapper;
import com.nervous.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>implements UserService {
}
