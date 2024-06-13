package com.visa.lib.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "items" , schema = "shopnow")
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

    @Column (name = "quantity")
    @NotNull(message = "quantity must not be null")
    private int quantity;

    @Column (name = "subtotal")
    @NotNull(message = "subTotal must not be null")
    private BigDecimal subTotal;

    @ManyToOne (fetch=FetchType.EAGER)
    @JoinColumn (name = "product_id")
    private Product product;

    @ManyToMany (mappedBy = "items")
    @JsonIgnore
    private List<Order> orders;

}
