package com.visa.orderService.controller;


import com.visa.lib.entity.Auth.UserAccount;
import com.visa.lib.entity.Item;
import com.visa.lib.entity.Order;
import com.visa.lib.entity.Product;
import com.visa.lib.exceptions.NotFoundException;
import com.visa.orderService.dto.ItemRequestDto;
import com.visa.orderService.dto.OrderDto;
import com.visa.orderService.feignclient.ProductClient;
import com.visa.orderService.feignclient.UserClient;
import com.visa.orderService.http.header.HeaderGenerator;
import com.visa.orderService.service.CartService;
import com.visa.orderService.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private ModelMapper modelMapper;

    @Autowired
    private HeaderGenerator headerGenerator;

    @PostMapping(value = "/{userId}")
    public ResponseEntity<OrderDto> saveOrder(
            @PathVariable("userId") Integer userId,
            @RequestBody List<ItemRequestDto> itemsDto,
            HttpServletRequest request) {
        UserAccount user = userClient.getUserById(userId);

        Set<Item> items = new HashSet<>();


        Order order = this.createOrder(user);
        Order finalOrder = orderService.saveOrder(order);
        itemsDto.stream().forEach(i -> {
            Product product = Optional.ofNullable(productClient.getProductById(i.getProduct()))
                    .orElseThrow(() -> new NotFoundException(String.format("Product with id[%d] not found", i.getProduct())));
            items.add(cartService.addItemToCar(finalOrder, product, i.getQuantity()));
        });

        finalOrder.setItems(items);
        OrderDto rs = modelMapper.map(finalOrder, OrderDto.class);
        return new ResponseEntity<OrderDto>(
                rs,
                headerGenerator.getHeadersForSuccessPostMethod(request, order.getId()),
                HttpStatus.CREATED);

    }


    private Order createOrder(UserAccount user) {
        Order order = Order.builder()
                .userId(user.getUserId())
                .orderedDate(LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .status("PAYMENT_EXPECTED")
                .build();
        order.setCreateAt(Instant.now());
        order.setUpdateAt(Instant.now());
        return order;
    }

    private Item createItem(ItemRequestDto dto, Product product, Order order) {
        Item item = Item.builder()
                .productId(product.getProductId())
                .orders(order)
                .quantity(dto.getQuantity())
                .subTotal(getSubTotalForItem(product, dto.getQuantity()))
                .build();
        item.setCreateAt(Instant.now());
        item.setUpdateAt(Instant.now());
        return item;
    }
}
