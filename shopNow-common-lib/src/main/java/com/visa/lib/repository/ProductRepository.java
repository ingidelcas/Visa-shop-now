package com.visa.lib.repository;

import com.visa.lib.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByCategoryCategoryTitle(String category);

    List<Product> findAllByProductName(String name);

    @Query(nativeQuery = true, value = "select case" +
            "  when p.quantity >0 then true" +
            "  else false " +
            " from products p" +
            " where p.product_id = :productId")
    Optional<Boolean> productIsAvailable(@Param("productId") Integer productId);


}
