package com.me.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.restaurant.entity.User;
import com.me.restaurant.mapper.UserMapper;
import com.me.restaurant.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
