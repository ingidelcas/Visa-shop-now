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
public class ProductSaveRequestDto {

    @Schema(description = "productName", example = "Sound System")
    @NotBlank(message = "productName cannot be blank")
    private String productName;

    @Schema(description = "price", example = "100000")
    @NotBlank(message = "price cannot be blank")
    private BigDecimal price;

    @Schema(description = "description", example = "Equipment for playing recorded music")
    @NotBlank(message = "description cannot be blank")
    private String description;

    @Schema(description = "quantity", example = "10")
    @NotNull(message = "quantity cannot be blank")
    private Integer quantity;

    @Schema(description = "category", example = "1")
    @NotNull(message = "category cannot be blank")
    private Integer category;
}
