package com.me.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.restaurant.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}