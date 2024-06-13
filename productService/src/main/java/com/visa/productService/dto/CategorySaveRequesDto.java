package com.visa.productService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategorySaveRequesDto {
    @Schema(description = "categoryTitle", example = "Consumer Electronics")
    @NotBlank(message = "CategoryTitle cannot be blank")
    private String categoryTitle;
}
