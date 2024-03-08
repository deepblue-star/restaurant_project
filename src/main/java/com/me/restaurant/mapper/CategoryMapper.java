package com.me.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.restaurant.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
