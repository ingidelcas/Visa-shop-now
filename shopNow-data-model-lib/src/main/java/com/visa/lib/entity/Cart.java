//package com.visa.lib.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.io.Serial;
//import java.math.BigDecimal;
//import java.util.Set;
//
//@Entity
//@Table(name = "carts")
//@NoArgsConstructor
//@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true, exclude = {"orders"})
//@Data
//@Builder
//public class Cart extends AbstractMappedEntity {
//
//    @Serial
//    private static final long serialVersionUID = 1L;
//
//    @EmbeddedId
//    private CartId id;
//
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<Item> items;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Set<Order> orders;
//
//    @Column(name = "total")
//    private BigDecimal total;
//
//}