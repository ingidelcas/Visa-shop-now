package com.visa.lib.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "items", schema = "shopnow")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Item extends AbstractMappedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "quantity")
    @NotNull(message = "quantity must not be null")
    private int quantity;

    @Column(name = "subtotal")
    @NotNull(message = "subTotal must not be null")
    private BigDecimal subTotal;


    @Column(name = "product_id")
    @NotNull(message = "productId must not be null")
    private Integer productId;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order orders;

}
