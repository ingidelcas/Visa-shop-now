package com.visa.orderService.controller;


import com.visa.lib.entity.Item;
import com.visa.lib.entity.Order;
import com.visa.lib.entity.Product;
import com.visa.lib.entity.auth.UserAccount;
import com.visa.lib.exceptions.NotFoundException;
import com.visa.orderService.dto.ItemRequestDto;
import com.visa.orderService.feignclient.ProductClient;
import com.visa.orderService.feignclient.UserClient;
import com.visa.orderService.http.header.HeaderGenerator;
import com.visa.orderService.service.CartService;
import com.visa.orderService.service.OrderService;
import com.visa.orderService.utilities.OrderUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.visa.orderService.utilities.CartUtilities.getSubTotalForItem;

@RestController
@RequestMapping(path = "/api/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private HeaderGenerator headerGenerator;

    @PostMapping(value = "/{userId}")
    public ResponseEntity<Order> saveOrder(
            @PathVariable("userId") Integer userId,
            @RequestBody List<ItemRequestDto> itemsDto,
            HttpServletRequest request) {
        UserAccount user = userClient.getUserById(userId);

        List<Item> items = new ArrayList<>();

        itemsDto.stream().forEach(i -> {
            Product product = Optional.ofNullable(productClient.getProductById(i.getProduct()))
                    .orElseThrow(() -> new NotFoundException(String.format("Product with id[%d] not found", i.getProduct())));
            items.add(createItem(i, product));
        });
        Order order = this.createOrder(items, user);

        orderService.saveOrder(order);

        return new ResponseEntity<Order>(
                order,
                headerGenerator.getHeadersForSuccessPostMethod(request, order.getId()),
                HttpStatus.CREATED);

    }


    private Order createOrder(List<Item> items, UserAccount user) {
        Order order = Order.builder()
                .user(user)
                .items(items)
                .orderedDate(LocalDateTime.now())
                .status("PAYMENT_EXPECTED")
                .total(OrderUtilities.countTotalPrice(items))
                .build();
        order.setCreateAt(Instant.now());
        order.setUpdateAt(Instant.now());
        return order;
    }

    private Item createItem(ItemRequestDto dto, Product product) {
        Item item = Item.builder()
                .product(product)
                .quantity(dto.getQuantity())
                .subTotal(getSubTotalForItem(product,dto.getQuantity()))
                .build();
        item.setCreateAt(Instant.now());
        item.setUpdateAt(Instant.now());
        return item;
    }
}
