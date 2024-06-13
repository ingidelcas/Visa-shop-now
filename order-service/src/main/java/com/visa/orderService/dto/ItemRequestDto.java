package com.visa.orderService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    @Schema(description = "product id", example = "1")
    @NotNull(message = "product canno be null")
    private Integer product;

    @Schema(description = "quantity", example = "3")
    @NotBlank(message = "quantity cannot be null")
    private int quantity;


}
