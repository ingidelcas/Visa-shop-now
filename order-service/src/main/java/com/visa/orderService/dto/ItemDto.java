package com.visa.orderService.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long id;

    private int quantity;


    private BigDecimal subTotal;


    private Integer productId;
}
