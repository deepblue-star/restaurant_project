package com.me.restaurant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.me.restaurant.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {
}
