package com.ecommerce.Ecom.repositories;

import com.ecommerce.Ecom.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Long> {

    //for nested model like cart has user aand user has email we need to write the query additionaly

    @Query("select c FROM  Cart c where c.user.email=?1")
    Cart findCartByEmail(String email);

    @Query("select c FROM  Cart c where c.user.email=?1 AND c.id=?2")
    Cart findCartByEmailAndCartId(String emailId, Long cartId);

    @Query("select c FROM  Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p where p.id=?1")
    List<Cart> findCartsByProductId(Long productId);



}
