package com.visa.orderService.controller;

import com.google.gson.Gson;
import com.visa.lib.entity.Item;
import com.visa.lib.entity.Order;
import com.visa.lib.entity.Product;
import com.visa.lib.entity.auth.UserAccount;
import com.visa.orderService.dto.ItemRequestDto;
import com.visa.orderService.feignclient.UserClient;
import com.visa.orderService.service.CartService;
import com.visa.orderService.service.OrderService;
import jakarta.servlet.http.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    private static final String PRODUCT_NAME = "test";
    private static final Integer PRODUCT_ID = 5;
    private static final Integer USER_ID = 1;
    private static final String USER_NAME = "Test";
    private static final Long CART_ID = 1L;
    Cookie mockCookie = Mockito.mock(Cookie.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CartService cartService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void save_order_controller_should_return201_when_valid_request() throws Exception {
        //given
        Product product = new Product();
        product.setProductId(PRODUCT_ID);
        product.setProductName(PRODUCT_NAME);

        UserAccount user = new UserAccount();
        user.setUserName(USER_NAME);

        Item item = new Item();
        item.setProduct(product);
        item.setQuantity(1);
        List<Item> cart = new ArrayList<Item>();
        cart.add(item);

        Order order = new Order();
        order.setItems(cart);
        order.setUser(user);


        ItemRequestDto itemDto = new ItemRequestDto(PRODUCT_ID, 1);
        Gson gson = new Gson();

        var payload = gson.toJson(Arrays.asList(itemDto));

        //when
        when(cartService.getAllItemsFromCart(CART_ID)).thenReturn(cart);
        when(userClient.getUserById(USER_ID)).thenReturn(user);
        when(orderService.saveOrder(new Order())).thenReturn(order);
        //then

        mockMvc.perform(post("/order/{userId}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.items").isArray());

        verify(orderService, times(1)).saveOrder(any(Order.class));
        verifyNoMoreInteractions(orderService);
    }
}
