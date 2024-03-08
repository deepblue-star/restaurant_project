package com.me.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.restaurant.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
