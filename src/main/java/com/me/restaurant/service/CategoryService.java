package com.me.restaurant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.me.restaurant.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
