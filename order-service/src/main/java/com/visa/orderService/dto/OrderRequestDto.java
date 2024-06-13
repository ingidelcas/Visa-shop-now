package com.visa.orderService.dto;

import com.visa.lib.entity.Item;
import com.visa.lib.entity.auth.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequestDto {

    @Schema(description = "DNI", example = "12345")
    @NotBlank(message = "DNI cannot be blank")
    private List<Item> items;
    @Schema(description = "DNI", example = "12345")
    @NotBlank(message = "DNI cannot be blank")
    private UserAccount user;
}
