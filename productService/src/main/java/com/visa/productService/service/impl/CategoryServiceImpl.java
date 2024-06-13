package com.visa.productService.service.impl;

import com.visa.lib.entity.Category;
import com.visa.lib.repository.CategoryRepository;
import com.visa.productService.dto.CategoryDto;
import com.visa.productService.dto.CategorySaveRequesDto;
import com.visa.productService.exception.CategoryNotFoundException;
import com.visa.productService.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public List<CategoryDto> findAll() {
        log.info("Category List Service, fetch all category");
        return categoryRepository.findAll().stream()
                .map(cat -> modelMapper.map(cat, CategoryDto.class))
                .distinct()
                .toList();
    }


    @Override
    public CategoryDto findById(Integer categoryId) {
        log.info("CategoryDto Service, fetch category by id");
        return categoryRepository.findById(categoryId)
                .map(cat -> modelMapper.map(cat, CategoryDto.class))
                .orElseThrow(() -> new CategoryNotFoundException(String.format("Category with id[%d] not found", categoryId)));
    }

    @Override
    public CategoryDto save(CategorySaveRequesDto categoryDto) {
        log.info("CategoryDto, service; save category");
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setCreateAt(Instant.now());
        category.setUpdateAt(Instant.now());
        return modelMapper.map(categoryRepository.save(category), CategoryDto.class);
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        log.info("CategoryDto Service, update category");

        Optional<Category> existingCategory = Optional.ofNullable(categoryRepository.findById(categoryDto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryDto.getCategoryId())));
        existingCategory.ifPresentOrElse(cat -> {
                    cat.setCategoryTitle(categoryDto.getCategoryTitle());
                },
                () -> {
                    throw new CategoryNotFoundException("Error updating category");
                }
        );

        return modelMapper.map(categoryRepository.save(existingCategory.get()), CategoryDto.class);

    }

    @Override
    public void deleteById(Integer categoryId) {
        log.info("Void Service, delete category by id");
        try {
            categoryRepository.deleteById(categoryId);
        } catch (CategoryNotFoundException e) {
            log.error("Error delete category", e);
            throw new CategoryNotFoundException("Error updating category", e);
        }
    }
}
