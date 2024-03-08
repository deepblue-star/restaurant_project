package com.me.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.restaurant.dto.DishDto;
import com.me.restaurant.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void updateDishStatusById(int status, List<Long> ids);
}
