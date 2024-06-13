package com.visa.lib.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.visa.lib.Utils.Constant;
import com.visa.lib.entity.auth.UserAccount;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders", schema = "shopnow")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Order extends AbstractMappedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = Constant.LOCAL_DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(pattern = Constant.LOCAL_DATE_TIME_FORMAT)
    @Column(name = "order_date")
    @NotNull
    private LocalDateTime orderedDate;

    @Column(name = "status")
    @NotNull
    private String status;

    @Column(name = "total")
    private BigDecimal total;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "carts", schema = "shopnow",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserAccount user;
}
