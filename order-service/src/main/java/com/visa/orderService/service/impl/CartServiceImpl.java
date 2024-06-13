package com.visa.orderService.service.impl;


import com.visa.lib.entity.Item;
import com.visa.lib.entity.Order;
import com.visa.lib.entity.Product;
import com.visa.lib.exceptions.NotFoundException;
import com.visa.lib.repository.ItemRepository;
import com.visa.lib.repository.OrderRepository;
import com.visa.orderService.feignclient.ProductClient;
import com.visa.orderService.service.CartService;
import com.visa.orderService.utilities.CartUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ItemRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Item> addItemToCart(Long orderId, Integer productId, Integer quantity) {
        Product product = Optional.ofNullable(productClient.getProductById(productId))
                .orElseThrow(() -> new NotFoundException(String.format("Product with id[%d] not found", productId)));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id[%d] not found", orderId)));

        Item item = Item.builder()
                .quantity(quantity)
                .product(product)
                .orders(Collections.singletonList(order))
                .subTotal(CartUtilities.getSubTotalForItem(product, quantity))
                .build();
        cartRepository.save(item);
        return order.getItems();
    }

    @Override
    public List<Item> getAllItemsFromCart(Long orderId) {
        return new ArrayList<>(cartRepository.findByOrdersId(orderId));
    }

    @Override
    public Item changeItemQuantity(Long cartId, Integer productId, Integer quantity) {
        Item item = Optional.ofNullable(cartRepository.findByIdAndProductId(cartId, productId))
                .orElseThrow(() -> new NotFoundException(String.format("Item with id[%d] not found", cartId)));

        item.setQuantity(quantity);
        item.setSubTotal(CartUtilities.getSubTotalForItem(item.getProduct(), quantity));
        item.setUpdateAt(Instant.now());
        return cartRepository.save(item);
    }

    @Override
    public void deleteItemFromCart(long item) {
        Item cart = cartRepository.findById(item)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id[%d] not found", item)));
        cartRepository.delete(cart);

    }

    @Override
    public boolean checkIfItemIsExist(Long cartId, Integer productId) {
        Item item = Optional.ofNullable(cartRepository.findByIdAndProductId(cartId, productId))
                .orElseThrow(() -> new NotFoundException(String.format("Item with id[%d] not found", cartId)));

        if (item != null) {
            return true;
        }

        return false;
    }


    @Override
    public void deleteCart(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Item with id[%d] not found", orderId)));

        orderRepository.delete(order);
    }
}
