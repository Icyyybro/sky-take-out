package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品和其相应的口味
     * @param dishDTO
     */
    @Override
    @Transactional      //事务注解，表示这个操作是事务
    public void saveWithFlavor(DishDTO dishDTO) {

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //向菜品表中插入1条数据
        dishMapper.insert(dish);
        //因为前端传回来的dishDTO并没有dishId，dishId是数据库生成的，所以我们要自己取出dishId
        //在DishMapper.xml中使用回显方法，将id赋值给dishDTO对象，这样对象的id属性就有。
        Long dishId = dish.getId();

        //向口味表中插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0) {
            //将dishId赋值给flavors中各个对象
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            //向口味表中插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}
