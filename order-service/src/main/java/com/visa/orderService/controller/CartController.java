package com.visa.orderService.controller;

import com.visa.lib.entity.Item;
import com.visa.orderService.dto.ItemRequestDto;
import com.visa.orderService.http.header.HeaderGenerator;
import com.visa.orderService.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/cart", produces = MediaType.APPLICATION_JSON_VALUE)
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    private HeaderGenerator headerGenerator;

    @GetMapping
    public ResponseEntity<List<Item>> getCart(@RequestParam(value = "order") Long order) {
        List<Item> cart = cartService.getAllItemsFromCart(order);
        if (!cart.isEmpty()) {
            return new ResponseEntity<List<Item>>(
                    cart,
                    headerGenerator.getHeadersForSuccessGetMethod(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<List<Item>>(
                headerGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND);
    }

    @PostMapping(params = {"order"})
    public ResponseEntity<List<Item>> addItemToCart(
            @RequestParam(value = "order") Long order,
            @RequestBody ItemRequestDto itemsDto,
            HttpServletRequest request) {
        List<Item> cart = cartService.addItemToCart(order, itemsDto.getProduct(), itemsDto.getQuantity());

        return new ResponseEntity<List<Item>>(
                cart,
                headerGenerator.getHeadersForSuccessGetMethod(),
                HttpStatus.OK);

    }

    @PutMapping(value="/addQuantity",params = {"itemId"})
    public ResponseEntity<Item> changeItemQuantity(
            @RequestParam(value = "itemId") Long item,
            @RequestBody ItemRequestDto itemsDto,
            HttpServletRequest request) {
       Item cart = cartService.changeItemQuantity(item, itemsDto.getProduct(), itemsDto.getQuantity());

        return new ResponseEntity<Item>(
                cart,
                headerGenerator.getHeadersForSuccessGetMethod(),
                HttpStatus.OK);

    }

    @DeleteMapping(value = "/item", params = "itemId")
    public ResponseEntity<Void> removeItemFromCart(
            @RequestParam("itemId") Long cartId) {
        List<Item> cart = cartService.getAllItemsFromCart(cartId);
        if (cart != null) {
            cartService.deleteItemFromCart(cartId);
            return new ResponseEntity<Void>(
                    headerGenerator.getHeadersForSuccessGetMethod(),
                    HttpStatus.OK);
        }
        return new ResponseEntity<Void>(
                headerGenerator.getHeadersForError(),
                HttpStatus.NOT_FOUND);
    }
}
