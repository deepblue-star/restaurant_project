package com.me.restaurant.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.me.restaurant.common.BaseContext;
import com.me.restaurant.common.R;
import com.me.restaurant.dto.SetmealDto;
import com.me.restaurant.entity.Category;
import com.me.restaurant.entity.Setmeal;
import com.me.restaurant.service.CategoryService;
import com.me.restaurant.service.SetmealDishService;
import com.me.restaurant.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @PostMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("套餐信息：{}", setmealDto.toString());

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache", allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("删除的套餐id: {}", ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }

    // 没有用线程池的批量停售
//    @PostMapping("/status/{status}")
//    public R<String> status(@PathVariable("status") int status, @RequestParam("ids") List<Long> ids) {
//        setmealService.updateSetmealStatusById(status, ids);
//        return R.success("售卖状态修改成功");
//    }

    // 用了线程池的批量停售
    // controller层循环调用service，让service里使用线程池处理多个停售修改。
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") int status, @RequestParam("ids") List<Long> ids) {
        CountDownLatch countDownLatch = new CountDownLatch(ids.size());

        log.info("Controller当前操作的用户id：" + BaseContext.getCurrentId().toString());
        Long userid = BaseContext.getCurrentId();

        for (Long id : ids) {
            setmealService.updateSetmealStatusById(countDownLatch, status, id, userid);
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return R.success("售卖状态修改成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache", key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
}
