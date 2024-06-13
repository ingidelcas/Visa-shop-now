package com.visa.productService.dto;

import com.visa.lib.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Integer categoryId;
    private String categoryTitle;
}
