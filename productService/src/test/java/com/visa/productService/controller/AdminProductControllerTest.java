package com.visa.productService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.visa.lib.entity.Category;
import com.visa.productService.dto.CategoryDto;
import com.visa.productService.dto.ProductDto;
import com.visa.productService.dto.ProductSaveRequestDto;
import com.visa.productService.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminProductControllerTest {
    @Autowired
    private ModelMapper modelMapper;
    private static final String PRODUCT_NAME = "test";
    private static final String PRODUCT_CATEGORY = "testCategory";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void add_product_controller_should_return201_when_product_isSaved() throws Exception {
        //given
        var category = Category.builder()
                .categoryId(1)
                .categoryTitle(PRODUCT_CATEGORY)
                .build();

        ProductDto product = ProductDto
                .builder()
                .productName(PRODUCT_NAME)
                .price(BigDecimal.valueOf(100))
                .description("test")
                .quantity(10)
                .category(modelMapper.map(category, CategoryDto.class))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(product);

        //when      
        when(productService.addProduct(new ProductSaveRequestDto())).thenReturn(product);

        //then
        mockMvc.perform(post("/admin/products").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.productName").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.category").value(PRODUCT_CATEGORY));

        verify(productService, times(1)).addProduct(any(ProductSaveRequestDto.class));
        verifyNoMoreInteractions(productService);
    }

    @Test
    public void add_product_controller_should_return400_when_product_isNull() throws Exception {
        //given
        ProductDto product = null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(product);

        //then
        mockMvc.perform(post("/admin/products").content(requestJson).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest());
    }
}
