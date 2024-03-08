package com.me.restaurant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.me.restaurant.entity.AddressBook;
import com.me.restaurant.mapper.AddressBookMapper;
import com.me.restaurant.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
