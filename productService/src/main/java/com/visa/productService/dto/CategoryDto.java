package com.visa.productService.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private Integer categoryId;
    private String categoryTitle;
}
