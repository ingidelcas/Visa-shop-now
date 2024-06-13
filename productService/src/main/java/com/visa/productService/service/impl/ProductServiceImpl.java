package com.visa.productService.service.impl;

import com.visa.lib.entity.Product;
import com.visa.lib.repository.CategoryRepository;
import com.visa.lib.repository.ProductRepository;
import com.visa.productService.dto.ProductDto;
import com.visa.productService.dto.ProductSaveRequestDto;
import com.visa.productService.exception.ProductNotFoundException;
import com.visa.productService.service.CategoryService;
import com.visa.productService.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductDto> getAllProduct() {
        return productRepository.findAll().stream()
                .map(product ->
                        modelMapper.map(product, ProductDto.class)
                )
                .distinct()
                .toList();
    }

    @Override
    public List<ProductDto> getAllProductByCategory(String category) {
        return productRepository.findAllByCategoryCategoryTitle(category).stream()
                .map(product ->
                        modelMapper.map(product, ProductDto.class)
                )
                .distinct()
                .toList();
    }

    @Override
    public ProductDto getProductById(Integer productId) {
        log.info("ProductDto, service; fetch product by id");
        return productRepository.findById(productId)
                .map(product -> modelMapper.map(product, ProductDto.class))
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product with id[%d] not found", productId)));
    }


    @Override
    public List<ProductDto> getAllProductsByName(String name) {
        return productRepository.findAllByProductName(name).stream()
                .map(product ->
                        modelMapper.map(product, ProductDto.class)
                )
                .distinct()
                .toList();
    }

    @Override
    public ProductDto addProduct(ProductSaveRequestDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        product.setCreateAt(Instant.now());
        product.setUpdateAt(Instant.now());
        product.setCategory(categoryRepository.findById(productDto.getCategory()).orElseThrow(() -> new RuntimeException("Category not found in the database.")));
        return modelMapper.map(productRepository.save(product), ProductDto.class);
    }

    public ProductDto updateProduct(ProductDto product) {
        Optional<Product> existingProduct = Optional.ofNullable(productRepository.findById(product.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + product.getProductId())));
        existingProduct.ifPresentOrElse(prod -> {
                    prod.setProductName(product.getProductName());

                },
                () -> {
                    throw new ProductNotFoundException("Error updating product");
                });

        return modelMapper.map(productRepository.save(existingProduct.get()), ProductDto.class);
    }

    @Override
    public void deleteProduct(Integer productId) {
        productRepository.deleteById(productId);
    }
}
