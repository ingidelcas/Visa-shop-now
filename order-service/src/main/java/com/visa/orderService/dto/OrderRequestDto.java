package com.visa.orderService.dto;

import com.visa.lib.entity.Auth.UserAccount;
import com.visa.lib.entity.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class OrderRequestDto {

    @Schema(description = "Items")
    private List<Item> items;
    @Schema(description = "user")
    @NotBlank(message = "user cannot be blank")
    private UserAccount user;
}
