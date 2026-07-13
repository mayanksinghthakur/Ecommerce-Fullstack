package com.ecommerce.Ecom.repositories;

import com.ecommerce.Ecom.model.Cart;
import com.ecommerce.Ecom.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
    CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);

    //when we delete something we need to tell to jpa that we are deleteing aomething
    //so annotate the query with modifying
    @Modifying
    @Query("DELETE FROM  CartItem ci where ci.cart.id=?1 AND ci.product.id=?2")
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);

    @Modifying
    @Query("DELETE FROM  CartItem ci   where ci.cart.id=?1")
   void deleteAllByCartId(Long cartId);
}
