package com.visa.orderService.utilities;

import com.visa.lib.entity.Product;

import java.math.BigDecimal;



public class CartUtilities {

    public static BigDecimal getSubTotalForItem(Product product, int quantity){
       return (product.getPrice()).multiply(BigDecimal.valueOf(quantity));
    }
}
