package com.ecommerce.Ecom.service;

import com.ecommerce.Ecom.payload.CartDto;
import com.ecommerce.Ecom.payload.CartItemDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
     CartDto addProductToCart(Long productId, Integer quantity);
     List<CartDto> getAllCarts();

    CartDto getCart(String emailId, Long cartId);

    @Transactional
    CartDto UpdateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);

    String createOrUpdateCartWithItems(List<CartItemDto> cartItems);
}
