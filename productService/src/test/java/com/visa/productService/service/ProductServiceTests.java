package com.visa.productService.service;

import com.visa.lib.entity.Category;
import com.visa.lib.entity.Product;
import com.visa.lib.repository.ProductRepository;
import com.visa.productService.dto.ProductDto;
import com.visa.productService.service.impl.ProductServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTests {

    private static final String PRODUCT_NAME = "test";
    private static final Integer PRODUCT_ID = 5;
    private static final String PRODUCT_CATEGORY = "testCategory";

    private List<Product> products;
    private Product product;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

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

        products = new ArrayList<Product>();
        products.add(product);
    }

    @Test
    public void get_all_product_test() {
        // Data preparation
        String productName = "test";

        Mockito.when(productRepository.findAll()).thenReturn(products);

        // Method call
        List<ProductDto> foundProducts = productService.getAllProduct();

        // Verification
        assertEquals(foundProducts.get(0).getProductName(), productName);
        Mockito.verify(productRepository, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void get_one_by_id_test() {
        // Data preparation
        Mockito.when(productRepository.getOne(PRODUCT_ID)).thenReturn(product);

        // Method call
        ProductDto found = productService.getProductById(PRODUCT_ID);

        // Verification
        assertEquals(found.getProductId(), PRODUCT_ID);
        Mockito.verify(productRepository, Mockito.times(1)).getOne(Mockito.anyInt());
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void get_all_product_by_category_test() {
        // Data preparation
        Mockito.when(productRepository.findAllByCategoryCategoryTitle(PRODUCT_CATEGORY)).thenReturn(products);

        //Method call
        List<ProductDto> foundProducts = productService.getAllProductByCategory(PRODUCT_CATEGORY);

        //Verification
        assertEquals(products.get(0).getCategory(), PRODUCT_CATEGORY);
        assertEquals(products.get(0).getProductName(), PRODUCT_NAME);
        Mockito.verify(productRepository, Mockito.times(1)).findAllByCategoryCategoryTitle(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(productRepository);
    }

    @Test
    public void get_all_products_by_name_test() {
        // Data preparation
        Mockito.when(productRepository.findAllByProductName(PRODUCT_NAME)).thenReturn(products);

        //Method call
        List<ProductDto> foundProducts = productService.getAllProductsByName(PRODUCT_NAME);

        //Verification
        assertEquals(foundProducts.get(0).getProductName(), PRODUCT_NAME);
        Mockito.verify(productRepository, Mockito.times(1)).findAllByProductName(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(productRepository);
    }

}
