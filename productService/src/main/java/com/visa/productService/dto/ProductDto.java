package com.visa.productService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Integer productId;

    @Schema(description = "categoryTitle", example = "Consumer Electronics")
    @NotBlank(message = "CategoryTitle cannot be blank")
    private String productName;

    @Schema(description = "categoryTitle", example = "Consumer Electronics")
    @NotBlank(message = "CategoryTitle cannot be blank")
    private BigDecimal price;

    @Schema(description = "categoryTitle", example = "Consumer Electronics")
    @NotBlank(message = "CategoryTitle cannot be blank")
    private String description;

    @Schema(description = "categoryTitle", example = "Consumer Electronics")
    @NotNull(message = "CategoryTitle cannot be blank")
    private Integer quantity;

    @Schema(description = "categoryTitle", example = "Consumer Electronics")
    @NotNull(message = "CategoryTitle cannot be blank")
    private CategoryDto category;



}
