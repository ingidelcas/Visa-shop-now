package com.visa.productService.service;

import java.util.List;

import com.visa.productService.dto.ProductDto;
import com.visa.productService.dto.ProductSaveRequestDto;

public interface ProductService {
    public List<ProductDto> getAllProduct();
    public List<ProductDto> getAllProductByCategory(String category);
    public ProductDto getProductById(Integer id);
    public List<ProductDto> getAllProductsByName(String name);
    public ProductDto addProduct(ProductSaveRequestDto product);
    public void deleteProduct(Integer productId);

}
