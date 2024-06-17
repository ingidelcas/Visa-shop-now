package com.visa.lib.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"category"})
@Data
@Builder
@Entity
@Table(name = "products",schema = "shopnow")
public class Product extends AbstractMappedEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "product_id", unique = true, nullable = false, updatable = false)
    private Integer productId;

    @NotBlank(message = "productName must not be blank")
    @Column (name = "product_name")
    private String productName;

    @Column (name = "price")
    @NotNull(message = "price must not be blank")
    private BigDecimal price;

    @Column (name = "description")
    private String description;

    @Column (name = "quantity")
    @NotNull
    private int quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

}
