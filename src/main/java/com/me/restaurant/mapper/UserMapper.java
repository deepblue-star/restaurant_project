package com.me.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.restaurant.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
