package com.me.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.restaurant.dto.SetmealDto;
import com.me.restaurant.entity.Setmeal;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void removeWithDish(List<Long> ids);

//    public void updateSetmealStatusById(int status, List<Long> ids);
    public void updateSetmealStatusById(CountDownLatch countDownLatch, int status, Long id, Long userid);
}
