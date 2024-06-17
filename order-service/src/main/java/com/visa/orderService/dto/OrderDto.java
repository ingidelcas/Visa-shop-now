package com.visa.orderService.dto;

import com.visa.lib.entity.Item;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private LocalDateTime orderedDate;
    private String status;
    private BigDecimal total;
    private Integer userId;
    private Set<ItemDto> items;
}
