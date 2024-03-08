package com.me.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.restaurant.entity.Stock;

public interface StockService extends IService<Stock> {
    public boolean reduceStock(Long dishId, int quantity, int version);
}
