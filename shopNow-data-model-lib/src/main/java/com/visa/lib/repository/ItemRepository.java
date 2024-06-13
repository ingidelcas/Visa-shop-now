package com.visa.lib.repository;


import com.visa.lib.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(nativeQuery = true, value = "select i.* from shopnow.carts c " +
            "inner join shopnow.items i on i.item_id = c.item_id " +
            "where c.order_id = :orderId")
    List<Item> findByOrdersId(Long orderId);

    @Query(nativeQuery = true, value = "select i.* " +
            "from  shopnow.items i " +
            "where i.item_id  = :itemId and i.product_id= :product")
    Item findByIdAndProductId(@Param("itemId")long itemId,  @Param("product") Integer product);

    @Query(nativeQuery = true, value = "select i.* from shopnow.carts c " +
            "inner join shopnow.items i on i.item_id = c.item_id " +
            "where c.order_id = :order and i.product_id= :product")
    Optional<Item> findByOrderIdAndProductId(@Param("order") long order, @Param("product") Integer product);


}
