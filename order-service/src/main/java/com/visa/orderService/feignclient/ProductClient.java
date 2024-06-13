package com.visa.orderService.feignclient;


import com.visa.lib.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "productService", url = "${PRODUCT_SERVICE_URL}")
public interface ProductClient {

    @GetMapping(value = "api/products/{id}")
    public Product getProductById(@PathVariable(value = "id") Integer productId);

}
