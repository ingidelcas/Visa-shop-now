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
import com.visa.orderService.utilities.OrderUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ItemRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Set<Item> addItemToCart(Long orderId, Integer productId, Integer quantity) {
        Product product = Optional.ofNullable(productClient.getProductById(productId))
                .orElseThrow(() -> new NotFoundException(String.format("Product with id[%d] not found", productId)));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(String.format("Order with id[%d] not found", orderId)));

        Item item = Item.builder()
                .quantity(quantity)
                .productId(product.getProductId())
                .orders(order)
                .subTotal(CartUtilities.getSubTotalForItem(product, quantity))
                .build();
        item.setCreateAt(Instant.now());
        item.setUpdateAt(Instant.now());
        item = cartRepository.save(item);
        order.setTotal(OrderUtilities.countTotalPrice(Collections.singleton(item)));
        order.getItems().add(item);
        orderRepository.save(order);
        return order.getItems();
    }

    @Override
    public Item addItemToCar(Order order, Product product, Integer quantity) {
        Item item = Item.builder()
                .quantity(quantity)
                .productId(product.getProductId())
                .orders(order)
                .subTotal(CartUtilities.getSubTotalForItem(product, quantity))
                .build();
        item.setCreateAt(Instant.now());
        item.setUpdateAt(Instant.now());
        return cartRepository.save(item);

    }

    @Override
    public Set<Item> getAllItemsFromCart(Long orderId) {
        return new HashSet<>(cartRepository.findByOrdersId(orderId));
    }

    @Override
    public Item changeItemQuantity(Long cartId, Integer productId, Integer quantity) {
        Item item = Optional.ofNullable(cartRepository.findByIdAndProductId(cartId, productId))
                .orElseThrow(() -> new NotFoundException(String.format("Item with id[%d] not found", cartId)));

        Product product = Optional.ofNullable(productClient.getProductById(productId))
                .orElseThrow(() -> new NotFoundException(String.format("Product with id[%d] not found", productId)));

        item.setQuantity(quantity);
        item.setSubTotal(CartUtilities.getSubTotalForItem(product, quantity));
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


}
