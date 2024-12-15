package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ShoppingCartService {
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);


    List<ShoppingCart> showShoppingCart();


    void delete(ShoppingCartDTO shoppingCartDTO);

    void clean();
}
