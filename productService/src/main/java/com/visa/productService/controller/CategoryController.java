package com.visa.productService.controller;

import com.visa.productService.dto.CategoryDto;
import com.visa.productService.dto.CategorySaveRequesDto;
import com.visa.productService.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private  CategoryService categoryService;

    // Get a list of all categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAll() {
        log.info("CategoryDto List, controller; fetch all categories");
        return ResponseEntity.ok(categoryService.findAll());
    }



    // Get detailed information of a specific category:
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> findById(@PathVariable("categoryId")
                                                @NotBlank(message = "Input must not be blank")
                                                @Valid final String categoryId) {
        log.info("CategoryDto, resource; fetch category by id");
        return ResponseEntity.ok(categoryService.findById(Integer.parseInt(categoryId)));
    }

    //     Create a new category
    @PostMapping
    public ResponseEntity<CategoryDto> save(@RequestBody @NotNull(message = "Input must not be NULL")
                                                  @Valid final CategorySaveRequesDto categoryDto) {
        log.info("CategoryDto, resource; save category");
        return ResponseEntity.ok(categoryService.save(categoryDto));
    }

    // Update information of all category
    @PutMapping
    public ResponseEntity<CategoryDto> update(@RequestBody
                                              @NotNull(message = "Input must not be NULL")
                                              @Valid final CategoryDto categoryDto) {
        log.info("CategoryDto, resource; update category");
        return ResponseEntity.ok(categoryService.update(categoryDto));
    }


    // Delete a category
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("categoryId") final String categoryId) {
        log.info("Boolean, resource; delete category by id");
        categoryService.deleteById(Integer.parseInt(categoryId));
        return ResponseEntity.ok(true);
    }

}