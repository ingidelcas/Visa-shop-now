package com.visa.orderService.service;

import com.visa.lib.entity.Item;

import java.util.List;


public interface CartService {

    List<Item> addItemToCart(Long orderId, Integer productId, Integer quantity);

    List<Item> getAllItemsFromCart(Long orderId);

    Item changeItemQuantity(Long cartId, Integer productId, Integer quantity);

    void deleteItemFromCart(long item);

    boolean checkIfItemIsExist(Long cartId, Integer productId);

    void deleteCart(Long orderId);
}
