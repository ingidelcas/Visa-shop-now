//package com.visa.lib.repository;
//
//import com.visa.lib.entity.Cart;
//import com.visa.lib.entity.CartId;
//import com.visa.lib.entity.Item;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface CartRepository extends JpaRepository<Cart, CartId> {
//
//    @Query(value = "select c.items form Cart c where c.orders.id= :orderId ")
//    Optional<List<Item>> findByOrderId(@Param("orderId") Integer orderId);
//
//
//    @Query(value = "select C form Cart c where c.orders.user.userId= :userId ")
//    Optional<List<Cart>> findByUserId(@Param("userId") Integer userId);
//
//}
