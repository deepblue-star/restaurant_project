package com.me.restaurant.entity;

import lombok.Data;

@Data
public class Stock {
    private Long DishId;

    private int stock;

    private int version;

}
