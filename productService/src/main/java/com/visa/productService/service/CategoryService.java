package com.visa.productService.service;

import com.visa.productService.dto.CategoryDto;
import com.visa.productService.dto.CategorySaveRequesDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto findById(final Integer categoryId);

    CategoryDto save(final CategorySaveRequesDto categoryDto);

    CategoryDto update(final CategoryDto categoryDto);

    void deleteById(final Integer categoryId);
}
