package com.me.restaurant.dto;

import com.me.restaurant.entity.Setmeal;
import com.me.restaurant.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
