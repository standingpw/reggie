package com.nervous.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nervous.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
