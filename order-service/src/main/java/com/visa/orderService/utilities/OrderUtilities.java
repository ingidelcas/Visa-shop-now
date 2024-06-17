package com.visa.orderService.utilities;

import com.visa.lib.entity.Item;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;


public class OrderUtilities {

    public static BigDecimal countTotalPrice(Set<Item> cart) {
        AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);
        cart.stream().forEach(item -> {
            total.set(total.get().add(item.getSubTotal()));
        });

        return total.get();
    }

}
