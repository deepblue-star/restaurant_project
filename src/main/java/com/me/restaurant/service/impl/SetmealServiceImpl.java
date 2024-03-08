package com.me.restaurant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.restaurant.common.BaseContext;
import com.me.restaurant.common.CustomException;
import com.me.restaurant.dto.SetmealDto;
import com.me.restaurant.entity.Setmeal;
import com.me.restaurant.entity.SetmealDish;
import com.me.restaurant.mapper.SetmealMapper;
import com.me.restaurant.service.SetmealDishService;
import com.me.restaurant.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(queryWrapper);
        if (count > 0) throw new CustomException("套餐正在售卖中，无法删除");

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId, ids);

        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

    // 没有用线程池的批量停售
//    @Override
//    public void updateSetmealStatusById(int status, List<Long> ids) {
//        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.in(ids != null, Setmeal::getId, ids);
//        List<Setmeal> list = this.list(queryWrapper);
//
//        for (Setmeal setmeal : list) {
//            if (setmeal != null) {
//                setmeal.setStatus(status);
//                this.updateById(setmeal);
//            }
//        }
//    }

    // 用了线程池的批量停售
    @Override
    @Async("asyncServiceExecutor")
    public void updateSetmealStatusById(CountDownLatch countDownLatch, int status, Long id, Long userid) {
        try {
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getId, id);
            List<Setmeal> list = this.list(queryWrapper);
            BaseContext.setCurrentId(userid);

            for (Setmeal setmeal : list) {
                if (setmeal != null) {
                    setmeal.setStatus(status);
                    this.updateById(setmeal);
                }
            }
        } finally {
            countDownLatch.countDown();
        }

    }
}
