package com.visa.productService.controller;

import com.visa.lib.entity.Category;
import com.visa.lib.entity.Product;
import com.visa.productService.dto.ProductDto;
import com.visa.productService.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    private  ModelMapper modelMapper;

    private static final String PRODUCT_NAME = "test";
    private static final Integer PRODUCT_ID = 5;
    private static final String PRODUCT_CATEGORY = "testCategory";
    private List<ProductDto> products;
    private ProductDto productDto;
    private Product product;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    public ProductControllerTests() {
    }

    @Before
    public void setUp() {
        var category = Category.builder()
                .categoryId(1)
                .categoryTitle(PRODUCT_CATEGORY)
                .build();

        product = Product.builder()
                .productId(PRODUCT_ID)
                .productName(PRODUCT_NAME)
                .category(category)
                .build();

        productDto = modelMapper.map(product, ProductDto.class);

        products = new ArrayList<ProductDto>();
        products.add(productDto);

    }

    @Test
    public void get_all_products_controller_should_return200_when_validRequest() throws Exception {
        //when
        when(productService.getAllProduct()).thenReturn(products);
//        given(productService.getAllProduct()).willReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id").value(PRODUCT_ID))
                .andExpect(jsonPath("$[0].productName").value(PRODUCT_NAME));

        verify(productService, Mockito.times(1)).getAllProduct();
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void get_all_products_controller_should_return404_when_productList_isEmpty() throws Exception {
        //given
        List<ProductDto> products = new ArrayList<ProductDto>();

        //when
        when(productService.getAllProduct()).thenReturn(products);

        //then
        mockMvc.perform(get("/products"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8));

        verify(productService, Mockito.times(1)).getAllProduct();
        verifyNoMoreInteractions(productService);
    }


    @Test
    public void get_all_product_by_category_controller_should_return200_when_validRequest() throws Exception {
        //when
        when(productService.getAllProductByCategory(PRODUCT_CATEGORY)).thenReturn(products);

        //then
        mockMvc.perform(get("/products").param("category", PRODUCT_CATEGORY))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[0].id").value(PRODUCT_ID))
                .andExpect(jsonPath("$[0].category").value(PRODUCT_CATEGORY));

        verify(productService, times(1)).getAllProductByCategory(anyString());
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void get_all_product_by_category_controller_should_return404_when_productList_isEmpty() throws Exception {
        //given
        List<ProductDto> products = new ArrayList<ProductDto>();

        //when
        when(productService.getAllProductsByName(PRODUCT_NAME)).thenReturn(products);

        //then
        mockMvc.perform(get("/products").param("category", PRODUCT_CATEGORY))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8));

        verify(productService, times(1)).getAllProductByCategory(anyString());
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void get_one_product_by_id_controller_should_return200_when_validRequest() throws Exception {
        //when
        when(productService.getProductById(anyInt())).thenReturn(productDto);

        //then
        mockMvc.perform(get("/products/{id}", PRODUCT_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.productName").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.category").value(PRODUCT_CATEGORY));

        verify(productService, times(1)).getProductById(PRODUCT_ID);
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void get_one_product_by_id_controller_should_return404_when_product_isNotExist() throws Exception {
        //when
        when(productService.getProductById(anyInt())).thenReturn(null);

        //then
        mockMvc.perform(get("/products/{id}", PRODUCT_ID))
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON_UTF8))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(PRODUCT_ID);
        verifyNoMoreInteractions(productService);
    }
}
