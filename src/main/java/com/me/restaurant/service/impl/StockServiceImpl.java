package com.me.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.restaurant.entity.Stock;
import com.me.restaurant.mapper.StockMapper;
import com.me.restaurant.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Autowired
    private StockService stockService;

    @Override
    public boolean reduceStock(Long dishId, int quantity, int version) {
        LambdaQueryWrapper<Stock> qw = new LambdaQueryWrapper<>();
        qw.eq(Stock::getDishId, dishId);
        Stock stock = stockService.getOne(qw);
        int oldQuantity = stock.getStock();
        if (oldQuantity < quantity) return false;
        Stock newStock = new Stock();
        newStock.setDishId(dishId);
        newStock.setStock(oldQuantity - quantity);
        newStock.setVersion(version + 1);

        LambdaQueryWrapper<Stock> qw2 = new LambdaQueryWrapper<>();
        qw2.eq(Stock::getDishId, newStock.getDishId()).eq(Stock::getVersion, version);
        boolean success = stockService.update(newStock, qw2);

        return success;
    }
}
