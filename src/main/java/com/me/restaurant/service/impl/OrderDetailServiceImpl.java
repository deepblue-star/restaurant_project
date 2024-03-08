package com.me.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.restaurant.entity.OrderDetail;
import com.me.restaurant.mapper.OrderDetailMapper;
import com.me.restaurant.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}