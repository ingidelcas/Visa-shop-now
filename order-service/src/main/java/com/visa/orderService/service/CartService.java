package com.visa.orderService.service;

import com.visa.lib.entity.Item;
import com.visa.lib.entity.Order;
import com.visa.lib.entity.Product;

import java.util.List;
import java.util.Set;


public interface CartService {

    Set<Item> addItemToCart(Long orderId, Integer productId, Integer quantity);

    Item addItemToCar(Order order, Product product, Integer quantity);

    Set<Item> getAllItemsFromCart(Long orderId);

    Item changeItemQuantity(Long cartId, Integer productId, Integer quantity);

    void deleteItemFromCart(long item);

    boolean checkIfItemIsExist(Long cartId, Integer productId);

}
