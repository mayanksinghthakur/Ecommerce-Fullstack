package com.ecommerce.Ecom.controller;

import com.ecommerce.Ecom.model.Cart;
import com.ecommerce.Ecom.payload.CartDto;
import com.ecommerce.Ecom.payload.CartItemDto;
import com.ecommerce.Ecom.repositories.CartRepository;
import com.ecommerce.Ecom.service.CartService;
import com.ecommerce.Ecom.util.AuthUtil;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Lob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private AuthUtil authUtil;
    @Autowired
    CartService cartService;

    @PostMapping("/cart/create")
    public ResponseEntity<String>createOrUpdateCart(@RequestBody List<CartItemDto>cartItems){
        String response=cartService.createOrUpdateCartWithItems(cartItems);
        return  new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto>addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity){
        CartDto cartDto=cartService.addProductToCart(productId,quantity);
        return  new ResponseEntity<CartDto>(cartDto, HttpStatus.CREATED);
    }


    @GetMapping("/carts")
    public ResponseEntity<List<CartDto>>getCarts(){
        List<CartDto> cartDtos=cartService.getAllCarts();
        return new ResponseEntity<List<CartDto>>(cartDtos,HttpStatus.FOUND);
    }



    //giving cart to the only loged in users
    //passing cartId for only future uses
    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDto>getCartById(){
        String emailId=authUtil.loggedInEmail();

        Cart cart=cartRepository.findCartByEmail(emailId);
        Long cartId=cart.getCartId();


        CartDto cartDto=cartService.getCart(emailId,cartId);
        return new ResponseEntity<CartDto>(cartDto,HttpStatus.OK);
    }

    //so we are just adding capability of adding quantity of application
    @PutMapping("/cart/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDto> updateCartProduct(@PathVariable Long productId,@PathVariable String operation){

        CartDto cartDto=cartService.UpdateProductQuantityInCart(productId,operation.equalsIgnoreCase("delete")?-1:1);

        return new ResponseEntity<CartDto>(cartDto,HttpStatus.OK);
    }

//from which cart product is to be deleted
    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId,@PathVariable Long productId) {

        String status=cartService.deleteProductFromCart(cartId,productId);
        return new ResponseEntity<String>(status,HttpStatus.OK);
    }
}
